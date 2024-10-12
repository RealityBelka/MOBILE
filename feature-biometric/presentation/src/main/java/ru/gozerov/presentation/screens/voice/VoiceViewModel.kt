package ru.gozerov.presentation.screens.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gozerov.core.coroutines.runCatchingNonCancellation
import ru.gozerov.core.viewmodel.BaseViewModel
import ru.gozerov.domain.usecases.GetNewVoiceUseCase
import ru.gozerov.domain.usecases.UploadVoiceUseCase
import ru.gozerov.presentation.screens.voice.models.VoiceAction
import ru.gozerov.presentation.screens.voice.models.VoiceEvent
import ru.gozerov.presentation.screens.voice.models.VoiceViewState
import javax.inject.Inject

class VoiceViewModel(
    private val uploadVoiceUseCase: UploadVoiceUseCase
): BaseViewModel<VoiceViewState, VoiceAction, VoiceEvent>(VoiceViewState()) {

    override fun obtainEvent(viewEvent: VoiceEvent) {
        viewModelScope.launch {
            when (viewEvent) {
                is VoiceEvent.Initialize -> {
                    viewState = viewState.copy(
                        step = viewEvent.step,
                        fail = viewEvent.fail,
                        numbers = mapData(step = viewEvent.step)
                    )
                }

                is VoiceEvent.StartRecording -> {
                    viewState = viewState.copy(isCapturing = true, isFinish = false)
                    viewAction = VoiceAction.OnStartRecording(viewState.step, viewState.numbers)
                }

                is VoiceEvent.StopRecording -> {
                    viewState = viewState.copy(isCapturing = false, isFinish = false)
                    viewAction = VoiceAction.OnStopRecording
                }

                is VoiceEvent.FinishRecording -> {
                    viewState = viewState.copy(isCapturing = false, isFinish = true)
                    runCatchingNonCancellation {
                        uploadVoiceUseCase.invoke(viewState.step)
                    }
                }
            }
        }
    }

    private fun mapData(step: Int): List<SelectableNumber> {
        return when (step) {
            1 -> (0..9).mapIndexed { ind, n -> SelectableNumber(n, ind == 0) }
            2 -> (9 downTo 0).mapIndexed { ind, n -> SelectableNumber(n, ind == 0) }
            3 -> (0..9).shuffled().mapIndexed { ind, n -> SelectableNumber(n, ind == 0) }
            else -> throw IllegalStateException("Illegal step (max: $MAX_STEPS)")
        }
    }

    companion object {

        private const val MAX_STEPS = 3

    }

    class Factory @Inject constructor(
        private val uploadVoiceUseCase: UploadVoiceUseCase
    ): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VoiceViewModel(uploadVoiceUseCase) as T
        }

    }

}