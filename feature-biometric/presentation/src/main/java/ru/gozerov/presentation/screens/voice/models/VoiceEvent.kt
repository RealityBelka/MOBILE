package ru.gozerov.presentation.screens.voice.models

sealed interface VoiceEvent {

    class Initialize(
        val step: Int,
        val fail: String?
    ) : VoiceEvent

    data object StartRecording : VoiceEvent

    data object StopRecording : VoiceEvent

    data object FinishRecording : VoiceEvent

}