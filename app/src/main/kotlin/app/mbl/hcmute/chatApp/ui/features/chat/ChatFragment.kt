package app.mbl.hcmute.chatApp.ui.features.chat

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import androidx.core.graphics.colorSpace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager
import app.mbl.hcmute.chatApp.databinding.FragmentChatBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import app.mbl.hcmute.chatApp.domain.entities.*
import app.mbl.hcmute.chatApp.ui.features.chat.chatKit.ChatAdapter
import app.mbl.hcmute.chatApp.ui.features.chat.chatKit.MarkDownProvider
import app.mbl.hcmute.chatApp.ui.features.chat.chatKit.MarkdownIncomingTextMessageViewHolder
import app.mbl.hcmute.chatApp.ui.features.conversation.ChatStartType
import app.mbl.hcmute.chatApp.util.extension.visible
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.stfalcon.chatkit.messages.MessageHolders
import com.stfalcon.chatkit.messages.MessagesListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
@OptIn(BetaOpenAI::class)
class ChatFragment : BaseVmDbFragment<ChatViewModel, FragmentChatBinding>() {
    private val userAuthor: Author by lazy { Author("UserId", "User", "") }
    private val botAuthor: Author by lazy { Author(ChatBot.gptAssistant.id, ChatBot.gptAssistant.name, "") }
    private val messagesAdapter: ChatAdapter by lazy {
        val messageHolders = MessageHolders()
        messageHolders.setIncomingTextConfig(MarkdownIncomingTextMessageViewHolder::class.java, R.layout.item_custom_incomming_text_message)
        ChatAdapter(userAuthor.id, messageHolders)
    }

    @Inject
    lateinit var dataStore: DataStoreManager

    @Inject
    lateinit var openAi: OpenAI

    @Inject
    lateinit var navigator: AppNavigator

    private val args: ChatFragmentArgs by navArgs()

    lateinit var jobResponse: Job
    lateinit var responseMessage: LocalChatMessage

    override fun getLayoutId() = R.layout.fragment_chat

    override val viewModel: ChatViewModel by viewModels()

    override fun initOnCreate(savedInstanceState: Bundle?) {
        super.initOnCreate(savedInstanceState)
        MarkDownProvider.initMarkDown(requireContext()) // add markdown support for TextView
        if (args.startType == ChatStartType.NEW.name || args.startType == ChatStartType.SCAN.name) {
            viewModel.createConversation(viewModel.conversation.value)
            val startMessage = createLocalMessage(ChatBot.gptAssistant.welcomeMessage, botAuthor)
            messagesAdapter.addToStart(startMessage, true)
            saveLocalMessage(startMessage)
        } else {
            viewModel.setCommonProgressBar(true)
            var bookmarkIndex = -1;
            lifecycleScope.launch(Dispatchers.IO) {
                viewModel.conversation.tryEmit(viewModel.getConversationById(args.convId))
                val listMessage: List<LocalChatMessage> = viewModel.getConversationMessages(args.convId)
                Timber.d("listMessage: $listMessage")
                withContext(Dispatchers.Main) {
                    listMessage.forEachIndexed { index, mess ->
                        Timber.d("messageAuthor: ${mess.messageAuthor}")
                        messagesAdapter.addToStart(mess, true)
                        if (mess.id == args.messId) bookmarkIndex = index
                    }
                    if (args.messId != null) {
                        val scrollPosition = messagesAdapter.itemCount - 2 - bookmarkIndex
                        binding.messages.smoothScrollToPosition(scrollPosition)
                    }
                }
                delay(1000)
                viewModel.setCommonProgressBar(false)
            }
        }
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
        binding.messages.setAdapter(messagesAdapter)

        messagesAdapter.enableSelectionMode { count ->
            if (count > 0) {
                binding.btnBookmark.visibility = View.VISIBLE
            } else {
                binding.btnBookmark.visibility = View.GONE
            }
        }

        args.scanText?.let { scanText ->
            viewModel.setTypedText(scanText)
            viewModel.sendClickCommand(ChatUiState.SendMessage)
        }
        //set Done button on keyboard
        /*        binding.etInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.uiSingleEvent.postValue(ChatUiState.SendMessage)
                true
            } else false
        }*/
    }

    private fun listen() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")
        }
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            showToast("Error: Your device do not support google voice command")
            Timber.d("Error: Your device do not support google voice command," + e.message)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.apply {
                    viewModel.setTypedText(get(0))
                    viewModel.sendClickCommand(ChatUiState.SendMessage)
                }
            }
        }
    }

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.clickEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ChatUiState.SendMessage -> {
                    val message = viewModel.typedText.value
                    if (message.isNotEmpty()) {
                        val localMessage = createLocalMessage(message, userAuthor)
                        messagesAdapter.addToStart(localMessage, true)
                        saveLocalMessage(localMessage)
                        viewModel.setTypedText("")
                        sendMessage()
                    }
                }

                is ChatUiState.Voice -> listen()
                is ChatUiState.BackToHome -> navigator.getNavController().navigateUp()
                is ChatUiState.AddToBookmark -> {
                    viewModel.createBookmark(viewModel.conversation.value.id, messagesAdapter.selectedMessages)
                    showToast("Added message to bookmark")
                    messagesAdapter.unselectAllItems()
                }

                is ChatUiState.StopResponseMessage -> {
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        jobResponse.cancel()
                        saveLocalMessage(responseMessage)
                        binding.btnStop.visibility = View.GONE
                        viewModel.isBotTyping.tryEmit(false)
                    }
                }
            }
        }
    }

    private fun sendMessage() {
        viewModel.isBotTyping.tryEmit(true)
        binding.btnStop.visibility = View.VISIBLE
        val requestMessages = createSendMessages()
        responseMessage = createLocalMessage("", botAuthor)
        messagesAdapter.addToStart(responseMessage, true) // add response message

        val chatCompletionRequest = ChatCompletionRequest(ModelId(ChatBot.CHAT_GPT_MODEL), requestMessages) //Init chat completion request
        jobResponse = viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            Timber.d("chatCompletionRequest: ${chatCompletionRequest.messages}")
            try {
                openAi.chatCompletions(chatCompletionRequest).collect {
                    it.choices[0].delta?.content?.let { newestMessage ->
                        responseMessage.messageContent += newestMessage
                        delay(150)
                        withContext(Dispatchers.Main) { messagesAdapter.update(responseMessage) }
                    }
                }
                saveLocalMessage(responseMessage)
                getConversationTitle()
            } catch (ex: Exception) {
                Timber.e(ex)
                withContext(Dispatchers.Main) {
                    messagesAdapter.addToStart(createLocalMessage("Error: ${ex.message}", botAuthor), true)
                }
            }
            viewModel.isBotTyping.tryEmit(false)
            withContext(Dispatchers.Main) { binding.btnStop.visibility = View.GONE }
        }
    }

    private fun saveLocalMessage(message: LocalChatMessage) {
        viewModel.updateConversation(viewModel.conversation.value.copy(lastUpdated = System.currentTimeMillis()))
        viewModel.createMessage(message.copy(conversationId = viewModel.conversation.value.id))
    }

    private suspend fun getConversationTitle(): String? {
        if (viewModel.conversation.value.title.isEmpty()) {
            val requestMessages = createSendMessages(2).toMutableList()
            requestMessages.add(ChatMessage(ChatRole.User, GET_CONVERSATION_TITLE_COMMAND))
            Timber.d("AAA$requestMessages")
            val response = openAi.chatCompletion(ChatCompletionRequest(ModelId(ChatBot.CHAT_GPT_MODEL), requestMessages))
            response.choices[0].message?.content?.let { responseTitle ->
                Timber.d("AAA response: $responseTitle")
                viewModel.updateConversation(
                    viewModel.conversation.value.copy(title = responseTitle.substringAfterLast(":").trim())
                )
            }
        }
        return null
    }

    private fun createSendMessages(count: Int = MAX_SEND_MESSAGE) = messagesAdapter.lastNumberItems(count).filter { it.item is LocalChatMessage }
        .map { it.item as LocalChatMessage }
        .map {
            val author = if (it.messageAuthor.authorId == botAuthor.authorId) ChatRole.Assistant else ChatRole.User
            it.toChatMessage(author)
        }

    companion object {
        const val GET_CONVERSATION_TITLE_COMMAND = "Please name the above conversation in about 10 words," +
                " In case you don't know what to name the conversation, do your best, please." +
                " The answer is written in the following the format: Title : Name"

        const val MAX_SEND_MESSAGE = 5
        const val REQUEST_CODE_SPEECH_INPUT = 1
    }
}