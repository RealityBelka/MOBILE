package ru.gozerov.domain.repositories

import kotlinx.coroutines.flow.SharedFlow
import ru.gozerov.domain.models.VoiceRecording

interface VoiceRepository {

    suspend fun uploadVoice(step: Int, numbers: List<Int>)

    suspend fun getNewVoiceFlow(): SharedFlow<VoiceRecording>

    suspend fun getVoices(): List<VoiceRecording>

}