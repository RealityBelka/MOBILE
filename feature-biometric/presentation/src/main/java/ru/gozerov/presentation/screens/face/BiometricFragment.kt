package ru.gozerov.presentation.screens.face

import android.Manifest
import android.graphics.Color
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
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import ru.gozerov.core.navigation.Screen
import ru.gozerov.core.navigation.launch
import ru.gozerov.presentation.databinding.FragmentBiometricBinding

class BiometricFragment : Fragment() {

    private var _binding: FragmentBiometricBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBiometricBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cameraPermissionRequest.launch(Manifest.permission.CAMERA)

    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private lateinit var imageCapture: ImageCapture

    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startCamera()
            } else {
                // Handle permission denial
            }
        }

    private val highAccuracyOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL) // Enables smile and eye open probabilities
        .build()

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider) // `viewFinder` is your PreviewView
            }
            imageCapture = ImageCapture.Builder()
                .setTargetRotation(binding.viewFinder.display.rotation) // Ensures the captured image has correct rotation
                .build()

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
                captureImage()
            } catch (exc: Exception) {
                Log.e("CameraXApp", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun detectFaces(image: InputImage) {
        val detector = FaceDetection.getClient(highAccuracyOpts)
        detector.process(image)

        detector.process(image)
            .addOnSuccessListener { faces ->
                for (face in faces) {
                    val bounds = face.boundingBox
                    val rotY = face.headEulerAngleY
                    val rotZ = face.headEulerAngleZ

                    // Provide advice based on detected face features
                    showAdvice(face)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FaceDetection", "Face detection failed", e)
                return@addOnFailureListener
            }
    }

    private fun showAdvice(face: Face) {
        val smileProb = face.smilingProbability
        val advice = if (smileProb != null && smileProb > 0.5) {
            binding.faceOval.setBorderColor(Color.GREEN)
            "You're smiling! Keep it up!"
        } else {
            binding.faceOval.setBorderColor(Color.RED)
            "Try to smile more!"
        }
        binding.root.postDelayed({
            captureImage()
        }, 100L)
        Log.e("aaaa", advice)
        // Show advice to the user
    }

    private fun captureImage() {
        // Check if imageCapture is initialized
        if (::imageCapture.isInitialized) {
            // Capture the image
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(requireContext()),
                object : ImageCapture.OnImageCapturedCallback() {
                    @OptIn(ExperimentalGetImage::class)
                    override fun onCaptureSuccess(imageProxy: ImageProxy) {
                        // Get the captured image
                        val image = imageProxy.image
                        val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                        val inputImage = InputImage.fromMediaImage(image!!, rotationDegrees)

                        // Perform face detection on the captured image
                        detectFaces(inputImage)

                        // Close the image proxy to free up resources
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

}