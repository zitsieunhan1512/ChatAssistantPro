package app.mbl.hcmute.chatApp.ui.firstScreen

import app.mbl.hcmute.base.common.UIState


sealed class FirstScreenUiState : UIState {
    object TermClicked : FirstScreenUiState()
    object PolicyClicked : FirstScreenUiState()
}