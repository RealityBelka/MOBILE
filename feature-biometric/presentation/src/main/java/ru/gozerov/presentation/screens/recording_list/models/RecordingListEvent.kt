package ru.gozerov.presentation.screens.recording_list.models

sealed interface RecordingListEvent {

    data object GetVoices: RecordingListEvent

}