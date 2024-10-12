package ru.gozerov.presentation.screens.face_capturing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.gozerov.core.coroutines.runCatchingNonCancellation
import ru.gozerov.core.viewmodel.BaseViewModel
import ru.gozerov.domain.usecases.CheckPhotoUseCase
import ru.gozerov.presentation.screens.face_capturing.models.FaceCapturingAction
import ru.gozerov.presentation.screens.face_capturing.models.FaceCapturingEvent
import ru.gozerov.presentation.screens.face_capturing.models.FaceCapturingViewState
import javax.inject.Inject

class FaceCapturingViewModel @Inject constructor(
    private val checkPhotoUseCase: CheckPhotoUseCase
) :
    BaseViewModel<FaceCapturingViewState, FaceCapturingAction, FaceCapturingEvent>(
        FaceCapturingViewState()
    ) {

    override fun obtainEvent(viewEvent: FaceCapturingEvent) {
        viewModelScope.launch {
            when (viewEvent) {
                is FaceCapturingEvent.CaptureFace -> {
                    viewAction = FaceCapturingAction.OnCapturedFace
                }

                is FaceCapturingEvent.SwitchCamera -> {
                    viewState = viewState.copy(isFrontCamera = !viewState.isFrontCamera)
                }

                is FaceCapturingEvent.CheckPhoto -> {
                    runCatchingNonCancellation {
                        checkPhotoUseCase.invoke(viewEvent.imageBitmap, viewEvent.rectF)
                    }
                        .onSuccess { result ->
                            viewState = viewState.copy(
                                isCapturingAllowed = result.isPhotoValid,
                                fail = result.fail
                            )
                        }

                        .onFailure { e ->
                            viewAction = FaceCapturingAction.ShowError(e.message.toString())
                        }
                }

                is FaceCapturingEvent.ShowFailureHint -> {
                    viewState = viewState.copy(fail = viewEvent.message, isCapturingAllowed = false)
                }
            }
        }
    }

    class Factory @Inject constructor(
        private val checkPhotoUseCase: CheckPhotoUseCase
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FaceCapturingViewModel(checkPhotoUseCase) as T
        }

    }

}