package ru.gozerov.presentation.screens.face_capturing.models

import android.graphics.Bitmap

sealed interface FaceCapturingEvent {

    data object SwitchCamera : FaceCapturingEvent

    class CheckPhoto(
        val imageBitmap: Bitmap
    ) : FaceCapturingEvent

    class ShowFailureHint(
        val message: String
    ) : FaceCapturingEvent

    data object CaptureFace : FaceCapturingEvent

}