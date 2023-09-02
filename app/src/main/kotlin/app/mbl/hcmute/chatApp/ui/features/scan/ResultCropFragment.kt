package app.mbl.hcmute.chatApp.ui.features.scan

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentResultCropBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import app.mbl.hcmute.chatApp.analyzer.TextAnalyzer
import app.mbl.hcmute.chatApp.ui.features.conversation.ChatStartType
import com.google.mlkit.vision.common.InputImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ResultCropFragment : BaseVmDbFragment<SharedViewModel, FragmentResultCropBinding>() {
    override fun getLayoutId() = R.layout.fragment_result_crop

    override val viewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var navigator: AppNavigator

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
    }

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.croppedImage.observe(viewLifecycleOwner) {
            binding.croppedResult.setImageBitmap(it)
            textRecognition(it)
            viewModel.setCommonProgressBar(true)
        }
        viewModel.sourceText.observe(viewLifecycleOwner) {
            binding.etScannedText.setText(it)
            viewModel.isScanCompleted.tryEmit(true)
            viewModel.setCommonProgressBar(false)
        }

        viewModel.clickEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ScanUiState.SendScanText -> {
                    binding.etScannedText.text.let { it ->
                        var message = it.toString()
                        if (!binding.etOptionScript.text.toString().isEmpty()) {
                            message = binding.etOptionScript.text.toString() + ":\n" + message
                        }
                        navigator.navigateTo(ResultCropFragmentDirections.actionResultCropFragmentToChatAssistantFragment(message, 0, null, ChatStartType.SCAN.name))
                    }
                }
            }
        }
    }

    private fun textRecognition(bitmap: Bitmap) {
        val textAnalyzer = TextAnalyzer(
            requireContext(),
            lifecycle,
            viewModel.sourceText,
        )
        textAnalyzer.recognizeText(InputImage.fromBitmap(bitmap, 0))
    }
}