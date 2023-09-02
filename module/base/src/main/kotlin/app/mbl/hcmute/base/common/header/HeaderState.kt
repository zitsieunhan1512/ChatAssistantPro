package app.mbl.hcmute.base.common.header

import app.mbl.hcmute.base.common.UIState

sealed class HeaderState : UIState {
    object BackPress : HeaderState()
    object TitlePress : HeaderState()
    object MenuPress : HeaderState()
}