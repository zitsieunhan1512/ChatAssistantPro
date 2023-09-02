package app.mbl.hcmute.chatApp.ui.features.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import app.mbl.hcmute.base.common.BaseVmDbFragment
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.databinding.FragmentScanBinding
import app.mbl.hcmute.chatApp.di.module.navigationModule.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

typealias LumaListener = (luma: Double) -> Unit

@AndroidEntryPoint
class ScanFragment : BaseVmDbFragment<SharedViewModel, FragmentScanBinding>() {
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun getLayoutId() = R.layout.fragment_scan

    override val viewModel: SharedViewModel by activityViewModels()

    @Inject
    lateinit var navigator: AppNavigator

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.vm = viewModel
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun setUpObservers() {
        super.setUpObservers()
        viewModel.clickEvent.observe(viewLifecycleOwner) {
            when (it) {
                is ScanUiState.CaptureImage -> {
                    takePhoto()
                }
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder().build().also { it.setSurfaceProvider(binding.viewFinder.surfaceProvider) }

            imageCapture = ImageCapture.Builder().setTargetRotation(requireView().display.rotation).build()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Timber.tag(TAG).e(exc, "Use case binding failed")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        val fileName = "capturedImage.jpg"
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(File(requireContext().cacheDir.path + "/" + fileName))
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Timber.tag(TAG).e(exc, "Photo capture failed: ${exc.message}")
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val uri = output.savedUri ?: return
                val msg = "Photo capture succeeded: ${output.savedUri}"
                Timber.tag(TAG).d(msg)
                navigator.navigateTo(ScanFragmentDirections.actionScanFragmentToCropperFragment(uri.toString()))
            }
        }
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                showToast("Permissions not granted by the user.")
                navigator.getNavController().navigateUp()
            }
        }
    }

    companion object {
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(Manifest.permission.CAMERA).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }.toTypedArray()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!cameraExecutor.isShutdown) cameraExecutor.shutdown()
    }
}