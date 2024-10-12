package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.VoiceRecording
import ru.gozerov.domain.repositories.VoiceRepository
import javax.inject.Inject

class GetVoicesUseCase @Inject constructor(
    private val voiceRepository: VoiceRepository
) {

    suspend operator fun invoke(): List<VoiceRecording> = withContext(Dispatchers.IO) {
        return@withContext voiceRepository.getVoices()
    }

}