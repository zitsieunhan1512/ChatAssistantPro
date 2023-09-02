package app.mbl.hcmute.chatApp.ui.features.scan

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.base.common.UIState
import app.mbl.hcmute.chatApp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor() : BaseViewModel() {
    private val _croppedImage = MutableLiveData<Bitmap>()
    val croppedImage: LiveData<Bitmap> = _croppedImage
    fun setCroppedImage(bitmap: Bitmap) {
        _croppedImage.postValue(bitmap)
    }

    val sourceText = MutableLiveData<String>()
    val isScanCompleted = MutableStateFlow(false)

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.btnCaptureImage -> _clickEvent.postValue(ScanUiState.CaptureImage)
            R.id.btnCrop -> _clickEvent.postValue(ScanUiState.CropImage)
            R.id.btnSendScanText -> _clickEvent.postValue(ScanUiState.SendScanText)
        }
    }

    fun sendClickCommand(command: UIState) {
        _clickEvent.postValue(command)
    }
}