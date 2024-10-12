package ru.gozerov.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.repositories.VoiceRepository
import javax.inject.Inject

class UploadVoiceUseCase @Inject constructor(
    private val voiceRepository: VoiceRepository
) {

    suspend operator fun invoke(step: Int): Unit = withContext(Dispatchers.IO) {
        return@withContext voiceRepository.uploadVoice(step)
    }

}