package ru.gozerov.presentation.screens.voice.models

import ru.gozerov.presentation.screens.voice.SelectableNumber

sealed interface VoiceAction {

    class OnStartRecording(
        val step: Int,
        val numbers: List<SelectableNumber>
    ): VoiceAction

    data object OnStopRecording: VoiceAction

}