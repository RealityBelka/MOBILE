package ru.gozerov.presentation.screens.face_capturing.models

data class FaceCapturingViewState(
    val isCapturingAllowed: Boolean = false,
    val fail: String? = null,
    val isFrontCamera: Boolean = true
)