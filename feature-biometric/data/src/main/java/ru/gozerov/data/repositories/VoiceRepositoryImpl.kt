package ru.gozerov.data.repositories

import android.content.Context
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.gozerov.data.api.VoiceApi
import ru.gozerov.domain.models.VoiceRecording
import ru.gozerov.domain.repositories.VoiceRepository
import java.io.File
import javax.inject.Inject
import kotlin.random.Random

class VoiceRepositoryImpl @Inject constructor(
    private val context: Context,
    private val voiceApi: VoiceApi
) : VoiceRepository {

    private val voices: MutableMap<Int, VoiceRecording> = mutableMapOf()

    private val _newVoice: MutableSharedFlow<VoiceRecording> = MutableSharedFlow(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override suspend fun uploadVoice(step: Int, numbers: List<Int>) {
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

    private fun getVoicePart(step: Int): MultipartBody.Part? {
        val cacheDir = context.externalCacheDir ?: return null
        val file = File(cacheDir, "record_$step.aac")

        if (!file.exists()) {
            return null
        }
        val mimeType = "audio/*"

        val requestBody: RequestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        val part: MultipartBody.Part = MultipartBody.Part.createFormData("audio", file.name, requestBody)

        return part
    }

}