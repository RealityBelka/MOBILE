package ru.gozerov.data.api

import retrofit2.http.GET

interface VoiceApi {

    @GET("voices")
    suspend fun getVoices(): List<String>

}