package ru.gozerov.data.repositories

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.gozerov.domain.models.VoiceRecording
import ru.gozerov.domain.repositories.VoiceRepository
import javax.inject.Inject
import kotlin.random.Random

class VoiceRepositoryImpl @Inject constructor() : VoiceRepository {

    private val voices: MutableMap<Int, VoiceRecording> = mutableMapOf()

    private val _newVoice: MutableSharedFlow<VoiceRecording> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override suspend fun uploadVoice(step: Int) {
        val isSuccess = true
        val voice = VoiceRecording(
            step = step,
            isSuccess = isSuccess,
            fail = if (!isSuccess) "Произошла ошибка" else null
        )
        voices[step] = voice
        _newVoice.emit(voice)
    }

    override suspend fun getNewVoiceFlow(): SharedFlow<VoiceRecording> = _newVoice.asSharedFlow()

    override suspend fun getVoices(): List<VoiceRecording> = voices.values.toList()

}