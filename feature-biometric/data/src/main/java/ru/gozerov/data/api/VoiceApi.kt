package ru.gozerov.data.api

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.gozerov.data.models.HintResponse

interface VoiceApi {

    @POST("voice")
    suspend fun checkVoice(
        @Body voice: RequestBody,
        @Header("Content-Type") type: String = "audio/aac"
    ): HintResponse

}