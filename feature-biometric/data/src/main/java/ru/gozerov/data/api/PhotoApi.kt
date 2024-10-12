package ru.gozerov.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import ru.gozerov.data.models.HintResponse

interface PhotoApi {

    @POST("face")
    suspend fun checkPhoto(
        @Body file: RequestBody,
        @Header("Content-Type") type: String = "image/jpeg"
    ): HintResponse

}