package ru.gozerov.presentation.screens.recording_list

data class Recording(
    val step: Int,
    val isSuccess: Boolean,
    val fail: String?,
    val pathToRecording: String,
    val isPlaying: Boolean = false
)
