package app.mbl.hcmute.chatApp.ui.features.setting

import app.mbl.hcmute.base.common.UIState


sealed class SettingUiState : UIState {
    object TermClicked : SettingUiState()
    object PolicyClicked : SettingUiState()
}