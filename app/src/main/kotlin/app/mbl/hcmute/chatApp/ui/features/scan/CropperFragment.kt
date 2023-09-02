package app.mbl.hcmute.chatApp.ui.features.scan

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentCropperBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CropperFragment : BaseVmDbFragment<SharedViewModel, FragmentCropperBinding>() {

    private val args: CropperFragmentArgs by navArgs()

    override fun getLayoutId() = R.layout.fragment_cropper

    override val viewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var navigator: AppNavigator

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.vm = viewModel
        binding.cropImageView.setImageUriAsync(Uri.parse(args.imgUri))
    }

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.clickEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ScanUiState.CropImage -> {
                    binding.cropImageView.getCroppedImage()?.let { it1 ->
                        viewModel.setCroppedImage(it1)
                        showToast("Successful photo cropping! Please wait for text recognition...")
                        navigator.navigateTo(CropperFragmentDirections.actionCropperFragmentToResultCropFragment())
                    }
                }
            }
        }
    }
}