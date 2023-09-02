package app.mbl.hcmute.chatApp.ui.features.conversation

import app.mbl.hcmute.base.common.UIState

sealed class ConversationUiState : UIState {
    object CreateConversationClick : ConversationUiState()
    object ScanDocumentClick : ConversationUiState()
    object ConversationClick : ConversationUiState()
    object TakePhotoClick : ConversationUiState()

}