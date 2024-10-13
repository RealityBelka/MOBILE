package ru.gozerov.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.gozerov.data.models.HintResponse

interface PhotoApi {

    @Multipart
    @POST("face")
    suspend fun checkPhoto(
        @Part photo: MultipartBody.Part?,
        @Part("numbers") numbers: RequestBody
    ): Response<HintResponse>

}