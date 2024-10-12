package ru.gozerov.domain.repositories

import android.graphics.Bitmap
import ru.gozerov.domain.models.CheckPhotoResult

interface PhotoRepository {

    suspend fun checkPhoto(bitmap: Bitmap): CheckPhotoResult

}