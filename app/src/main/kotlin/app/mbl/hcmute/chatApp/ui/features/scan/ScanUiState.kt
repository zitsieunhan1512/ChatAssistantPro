package app.mbl.hcmute.chatApp.ui.features.scan

import app.mbl.hcmute.base.common.UIState

sealed class ScanUiState : UIState {
    object CaptureImage : ScanUiState()
    object CropImage : ScanUiState()
    object SendScanText : ScanUiState()

}