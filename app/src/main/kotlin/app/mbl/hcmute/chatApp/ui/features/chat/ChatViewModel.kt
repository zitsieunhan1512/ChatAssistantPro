package app.mbl.hcmute.chatApp.ui.features.chat

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager
import app.mbl.hcmute.chatApp.data.repository.ChatRepository
import app.mbl.hcmute.chatApp.domain.entities.ChatBookmark
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import app.mbl.hcmute.chatApp.domain.entities.LocalChatMessage
import com.aallam.openai.client.OpenAI
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    val openAi: OpenAI,
    private val chatRepository: ChatRepository,
) : BaseViewModel() {

    val typedText = MutableStateFlow("")
    val isBotTyping = MutableStateFlow(false)
    val conversation = MutableStateFlow(Conversation(id = System.nanoTime(), lastUpdated = System.currentTimeMillis()))

    fun createConversation(conversation: Conversation) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.createConversation(conversation)
        }
    }

    suspend fun getConversationById(convId: Long): Conversation {
        return chatRepository.getConversation(convId)
    }

    fun updateConversation(data: Conversation) {
        conversation.tryEmit(data)
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.updateConversation(data)
        }
    }

    fun createBookmark(conversationId: Long, messages: List<LocalChatMessage>) {
        viewModelScope.launch(Dispatchers.IO) {
            messages.forEach {
                val chatBookmark = ChatBookmark(
                    id = it.id,
                    messageId = it.id,
                    conversationId = conversationId,
                    content = it.messageContent ?: "",
                    createdTime = System.currentTimeMillis(),
                )
                chatRepository.createBookmark(chatBookmark)
            }
        }
    }

    fun createMessage(message: LocalChatMessage) {
        Timber.d("createMessage: $message")
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.createMessage(message)
        }
    }

    fun getConversationMessages(id: Long) = chatRepository.getMessages(id)

    fun setTypedText(text: String) {
        if (text == typedText.value) return
        typedText.tryEmit(text)
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btnSend -> _clickEvent.postValue(ChatUiState.SendMessage)
            R.id.btnVoice -> _clickEvent.postValue(ChatUiState.Voice)
            R.id.btnBackToHome -> _clickEvent.postValue(ChatUiState.BackToHome)
            R.id.btnBookmark -> _clickEvent.postValue(ChatUiState.AddToBookmark)
            R.id.btnStop -> _clickEvent.postValue(ChatUiState.StopResponseMessage)
        }
    }

    fun sendClickCommand(command: ChatUiState) {
        _clickEvent.postValue(command)
    }
}