package app.mbl.hcmute.chatApp.ui.features.conversation

import android.view.View
import androidx.lifecycle.viewModelScope
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.data.repository.ChatRepository
import app.mbl.hcmute.chatApp.domain.entities.Conversation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(private val chatRepository: ChatRepository) : BaseViewModel() {

    val conversations = chatRepository.getConversations()

    override fun onClick(view: View) {
        when (view.id) {
            R.id.createConversation -> uiSingleEvent.postValue(ConversationUiState.CreateConversationClick)
            R.id.scanDocument -> uiSingleEvent.postValue(ConversationUiState.ScanDocumentClick)
        }
    }

    fun deleteConversation(conversation: Conversation) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.deleteConversation(conversation)
        }
    }

    fun createConversation(conversation: Conversation) {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.createConversation(conversation)
        }
    }
}