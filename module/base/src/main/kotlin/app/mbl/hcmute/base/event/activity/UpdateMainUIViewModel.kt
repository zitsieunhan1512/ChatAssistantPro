package app.mbl.hcmute.base.event.activity

import app.mbl.hcmute.base.common.BaseViewModel

class UpdateMainUIViewModel : BaseViewModel() {

    fun hideHeaderView(isHide: Boolean = true) {
        _uiState.value = MainUIState.HideHeaderView(isHide)
    }
}