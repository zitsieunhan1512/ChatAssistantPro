package app.mbl.hcmute.chatApp.ui.features.conversation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.base.ext.observe
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentConversationBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import app.mbl.hcmute.chatApp.ui.features.conversation.conversationsProvider.ConversationBinder
import app.mbl.hcmute.chatApp.ui.firstScreen.FirstScreenFragmentDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mva3.adapter.ListSection
import mva3.adapter.MultiViewAdapter
import javax.inject.Inject


@AndroidEntryPoint
class ConversationFragment : BaseVmDbFragment<ConversationViewModel, FragmentConversationBinding>() {
    private val conversationAdapter = MultiViewAdapter()
    private val conversationSection = ListSection<Conversation>()
    override val viewModel: ConversationViewModel by viewModels()
    override fun getLayoutId() = R.layout.fragment_conversation

    @Inject
    lateinit var navigator: AppNavigator

    @SuppressLint("RestrictedApi")
    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
        binding.rvConversation.apply {
//            addItemDecoration(BottomItemDecoration())
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = conversationAdapter
            conversationAdapter.registerItemBinders(ConversationBinder(onItemClick))
            conversationAdapter.removeAllSections()
            conversationAdapter.addSection(conversationSection)
        }
        conversationAdapter.itemTouchHelper.attachToRecyclerView(binding.rvConversation)
        conversationSection.setSwipeToDismissListener { position, item ->
            showUndoSnackBar(position, item)
        }
    }

    private fun showUndoSnackBar(position: Int, deleteItem: Conversation) {
        var deleteConversation = true
        Snackbar.make(binding.root, "Will delete conversation after 3 seconds!", Snackbar.LENGTH_SHORT).apply {
            setAction(getString(R.string.undo)) {
                deleteConversation = false
                conversationSection.add(position, deleteItem)
            }.addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (deleteConversation) viewModel.deleteConversation(deleteItem)
                }
            }).show()
        }
    }

    private val onItemClick: (id: Long) -> Unit = {
        val direction = FirstScreenFragmentDirections.actionFirstScreenFragmentToChatAssistantFragment(null, it, null, ChatStartType.CONVERSATION.name)
        navigator.navigateTo(direction)
    }

    override fun setUpObservers() {
        super.setUpObservers()
        observe(viewModel.conversations) { conversationSection.set(it) }
        observe(viewModel.uiSingleEvent) {
            when (it) {
                ConversationUiState.CreateConversationClick -> {
                    showToast("Start ChatGpt screen")
                    val direction = FirstScreenFragmentDirections.actionFirstScreenFragmentToChatAssistantFragment(null, -1, null, ChatStartType.NEW.name)
                    navigator.navigateTo(direction)
                }

                ConversationUiState.ScanDocumentClick -> {
                    showToast("Scan document")
                    val direction = FirstScreenFragmentDirections.actionFirstScreenFragmentToScanFragment()
                    navigator.navigateTo(direction)
                }

                else -> {}
            }
        }
    }
}