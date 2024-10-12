package ru.gozerov.presentation.screens.recording_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gozerov.core.coroutines.runCatchingNonCancellation
import ru.gozerov.core.viewmodel.BaseViewModel
import ru.gozerov.domain.usecases.GetNewVoiceUseCase
import ru.gozerov.domain.usecases.GetVoicesUseCase
import ru.gozerov.presentation.screens.recording_list.models.RecordingListAction
import ru.gozerov.presentation.screens.recording_list.models.RecordingListEvent
import ru.gozerov.presentation.screens.recording_list.models.RecordingListViewState
import javax.inject.Inject

class RecordingListViewModel(
    private val getNewVoiceUseCase: GetNewVoiceUseCase,
    private val getVoicesUseCase: GetVoicesUseCase
) :
    BaseViewModel<RecordingListViewState, RecordingListAction, RecordingListEvent>(
        RecordingListViewState()
    ) {

    override fun obtainEvent(viewEvent: RecordingListEvent) {
        viewModelScope.launch {
            when (viewEvent) {
                is RecordingListEvent.GetVoices -> {

                    launch {
                        runCatchingNonCancellation {
                            getVoicesUseCase.invoke()
                        }
                            .onSuccess { voices ->
                                val areAllSuccess =
                                    voices.size == 3 && voices.all { v -> v.isSuccess }
                                viewState =
                                    viewState.copy(voices = voices, areAllSuccess = areAllSuccess)
                            }
                            .onFailure { e ->
                                viewAction = RecordingListAction.ShowError(e.message.toString())
                            }
                    }

                    launch {
                        runCatchingNonCancellation {
                            getNewVoiceUseCase.invoke()
                        }
                            .onSuccess { flow ->
                                flow.collect { voice ->
                                    val voices = viewState.voices.toMutableList()

                                    if (viewState.voices.size < voice.step) voices.add(voice)
                                    else voices[voice.step - 1] = voice

                                    val areAllSuccess =
                                        voices.size == 3 && voices.all { v -> v.isSuccess }
                                    viewState =
                                        viewState.copy(
                                            voices = voices,
                                            areAllSuccess = areAllSuccess
                                        )

                                    viewState = viewState.copy(
                                        voices = voices,
                                        areAllSuccess = areAllSuccess
                                    )
                                }
                            }
                            .onFailure { e ->
                                viewAction = RecordingListAction.ShowError(e.message.toString())
                            }
                    }

                }
            }
        }
    }


    class Factory @Inject constructor(
        private val getNewVoiceUseCase: GetNewVoiceUseCase,
        private val getVoicesUseCase: GetVoicesUseCase
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return RecordingListViewModel(getNewVoiceUseCase, getVoicesUseCase) as T
        }

    }

}