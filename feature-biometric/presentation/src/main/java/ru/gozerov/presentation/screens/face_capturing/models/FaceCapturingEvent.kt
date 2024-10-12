package ru.gozerov.presentation.screens.face_capturing.models

import android.graphics.Bitmap
import android.graphics.RectF

sealed interface FaceCapturingEvent {

    data object SwitchCamera : FaceCapturingEvent

    class CheckPhoto(
        val imageBitmap: Bitmap,
        val rectF: RectF
    ) : FaceCapturingEvent

    class ShowFailureHint(
        val message: String
    ) : FaceCapturingEvent

    data object CaptureFace : FaceCapturingEvent

}