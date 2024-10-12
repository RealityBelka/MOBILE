package ru.gozerov.data.repositories

import android.content.Context
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.gozerov.data.api.VoiceApi
import ru.gozerov.domain.models.VoiceRecording
import ru.gozerov.domain.repositories.VoiceRepository
import java.io.File
import javax.inject.Inject

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
        val response = voiceApi.checkVoice(getVoiceBody(step))
        val voice = VoiceRecording(step, response.ok, response.message)
        voices[step] = voice
        _newVoice.emit(voice)
    }

    override suspend fun getNewVoiceFlow(): SharedFlow<VoiceRecording> = _newVoice.asSharedFlow()

    override suspend fun getVoices(): List<VoiceRecording> = voices.values.toList()

    private fun getVoiceBody(step: Int): RequestBody {
        val cacheDir = context.externalCacheDir
        val file = File(cacheDir, "record_$step.aac")

        return file.asRequestBody("audio/aac".toMediaTypeOrNull())
    }

}