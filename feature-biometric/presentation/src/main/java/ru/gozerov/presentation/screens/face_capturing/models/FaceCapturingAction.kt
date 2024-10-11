package ru.gozerov.presentation.screens.face_capturing.models

sealed interface FaceCapturingAction {

    data object OnCapturedFace: FaceCapturingAction

    class ShowError(val message: String): FaceCapturingAction

}