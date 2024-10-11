package ru.gozerov.presentation.screens.face_capturing.models

sealed interface FaceCapturingEvent {

    data object SwitchCamera: FaceCapturingEvent

    data object CheckPhoto: FaceCapturingEvent

    data object CaptureFace: FaceCapturingEvent

}