package ru.gozerov.data.api

import retrofit2.http.GET

interface PhotoApi {

    @GET("photos")
    suspend fun getPhotos(): List<String>

}