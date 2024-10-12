package ru.gozerov.presentation.screens.face_capturing

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.launch
import ru.gozerov.core.di.biometricComponentHolder
import ru.gozerov.presentation.R
import ru.gozerov.presentation.databinding.FragmentFaceCapturingBinding
import ru.gozerov.presentation.screens.face_capturing.models.FaceCapturingAction
import ru.gozerov.presentation.screens.face_capturing.models.FaceCapturingEvent
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

class FaceCapturingFragment : Fragment() {

    private var _binding: FragmentFaceCapturingBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var factory: FaceCapturingViewModel.Factory

    private val viewModel: FaceCapturingViewModel by viewModels { factory }

    private lateinit var imageCapture: ImageCapture
    private var image: InputImage? = null

    override fun onAttach(context: Context) {
        val component = context.biometricComponentHolder
        component.inject(this)

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFaceCapturingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cameraPermissionRequest.launch(Manifest.permission.CAMERA)

        var isFrontCamera = viewModel.viewStates().value.isFrontCamera

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.viewStates().collect { viewState ->

                        if (isFrontCamera != viewState.isFrontCamera) {
                            isFrontCamera = viewState.isFrontCamera
                            changeCamera(viewState.isFrontCamera)
                        }

                        binding.takePhotoButton.background =
                            ContextCompat.getDrawable(
                                requireContext(),
                                if (viewState.isCapturingAllowed) R.drawable.ic_take_photo_active else R.drawable.ic_take_photo_disable
                            )

                        binding.txtCard.visibility =
                            if (viewState.fail != null || viewState.isCapturingAllowed) View.VISIBLE else View.INVISIBLE

                        binding.txtCard.text =
                            if (viewState.isCapturingAllowed) getString(R.string.you_could_take_photo) else viewState.fail

                        val drawableStart = ContextCompat.getDrawable(
                            requireContext(),
                            if (viewState.isCapturingAllowed) R.drawable.ic_check else R.drawable.ic_alert
                        )
                        binding.txtCard.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            drawableStart,
                            null,
                            null,
                            null
                        )

                    }
                }

                launch {
                    viewModel.viewActions().collect { viewAction ->
                        when (viewAction) {
                            is FaceCapturingAction.OnCapturedFace -> {
                                image?.let { capturedImage ->
                                    saveInputImageToCache(capturedImage)
                                    viewModel.clearAction()
                                    findNavController().navigate(R.id.action_face_capturing_to_photoCheckFragment)
                                }
                            }

                            is FaceCapturingAction.ShowError -> {}

                            null -> {}
                        }
                    }
                }
            }
        }

        binding.switchCameraButton.setOnClickListener {
            viewModel.obtainEvent(FaceCapturingEvent.SwitchCamera)
        }

        binding.takePhotoButton.setOnClickListener {
            if (viewModel.viewStates().value.isCapturingAllowed) {
                viewModel.obtainEvent(FaceCapturingEvent.CaptureFace)
            }
        }

        binding.exitButton.setOnClickListener {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.startPageFragment, true).build()
            findNavController().navigate(
                resId = R.id.action_face_capturing_to_startPageFragment,
                args = null,
                navOptions = navOptions
            )
        }

    }

    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                val cameraSelector =
                    if (viewModel.viewStates().value.isFrontCamera) CameraSelector.DEFAULT_FRONT_CAMERA else CameraSelector.DEFAULT_BACK_CAMERA
                startCamera(cameraSelector)
            } else {
                // Handle permission denial
            }
        }

    private val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL) // Enables smile and eye open probabilities
        .build()

    private fun changeCamera(isFrontCamera: Boolean) {
        val cameraSelector = if (isFrontCamera) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }

        startCamera(cameraSelector)
    }


    private fun startCamera(cameraSelector: CameraSelector) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            _binding?.let {
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider) // `viewFinder` is your PreviewView
                }
                imageCapture = ImageCapture.Builder()
                    .setTargetRotation(binding.viewFinder.display.rotation) // Ensures the captured image has correct rotation
                    .build()

                try {
                    cameraProvider?.unbindAll()
                    cameraProvider?.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                    captureImage()
                } catch (exc: Exception) {
                    Log.e("CameraXApp", "Use case binding failed", exc)
                }
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun captureImage() {
        if (::imageCapture.isInitialized) {
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageCapturedCallback() {
                    @OptIn(ExperimentalGetImage::class)
                    override fun onCaptureSuccess(imageProxy: ImageProxy) {
                        val image = imageProxy.image
                        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                        val inputImage = InputImage.fromMediaImage(image!!, rotationDegrees)

                        this@FaceCapturingFragment.image = inputImage
                        detectFaces(inputImage)

                        imageProxy.close()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e(
                            "ImageCapture",
                            "Image capture failed: ${exception.message}",
                            exception
                        )
                    }
                }
            )
        }
    }

    private fun detectFaces(image: InputImage) {
        val detector = FaceDetection.getClient(highAccuracyOpts)
        detector.process(image)

        detector.process(image)
            .addOnSuccessListener { faces ->
                _binding?.let {

                    if (faces.isEmpty())
                        viewModel.obtainEvent(FaceCapturingEvent.ShowFailureHint(getString(R.string.no_person)))
                    else if (faces.size > 1)
                        viewModel.obtainEvent(FaceCapturingEvent.ShowFailureHint(getString(R.string.more_that_one_face)))
                    else {
                        val face = faces.first()
                        if (face.leftEyeOpenProbability == null || face.rightEyeOpenProbability == null ||
                            (face.leftEyeOpenProbability != null && face.leftEyeOpenProbability!! < 0.5f) ||
                            (face.rightEyeOpenProbability != null && face.rightEyeOpenProbability!! < 0.5f)
                        ) {
                            viewModel.obtainEvent(FaceCapturingEvent.ShowFailureHint(getString(R.string.your_eyes_are_closed)))
                        } else {
                            image.bitmapInternal?.let { bitmap: Bitmap ->
                                viewModel.obtainEvent(
                                    FaceCapturingEvent.CheckPhoto(
                                        bitmap,
                                        binding.faceOval.getOvalRect()
                                    )
                                )
                            }
                        }
                    }

                    binding.root.postDelayed({ if (_binding != null) captureImage() }, 200L)
                }

            }
            .addOnFailureListener { e ->
                Log.e("FaceDetection", "Face detection failed", e)
                return@addOnFailureListener
            }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun saveInputImageToCache(inputImage: InputImage) {
        var bitmap: Bitmap = inputImage.bitmapInternal ?: return
        if (viewModel.viewStates().value.isFrontCamera) {
            val matrix = Matrix().apply {
                preScale(-1f, 1f)
            }
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, false)
        }

        val cacheDir = requireActivity().externalCacheDir
        if (cacheDir != null) {
            val file = File(cacheDir, "face.jpeg")
            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos)
                fos.flush()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                fos?.close()
            }
        }
    }

}