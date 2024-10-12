package ru.gozerov.presentation.screens.recording_list.models

sealed interface RecordingListAction {

    class ShowError(val message: String) : RecordingListAction

}