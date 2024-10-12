package ru.gozerov.presentation.screens.recording_list.models

import ru.gozerov.domain.models.VoiceRecording

data class RecordingListViewState(
    val voices: List<VoiceRecording> = emptyList(),
    val areAllSuccess: Boolean = false
)
