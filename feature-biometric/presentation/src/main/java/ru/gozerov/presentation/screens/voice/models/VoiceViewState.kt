package ru.gozerov.presentation.screens.voice.models

import ru.gozerov.presentation.screens.voice.SelectableNumber

data class VoiceViewState(
    val step: Int = 0,
    val fail: String? = null,
    val numbers: List<SelectableNumber> = emptyList(),
    val isCapturing: Boolean = false,
    val isFinish: Boolean = false
)
