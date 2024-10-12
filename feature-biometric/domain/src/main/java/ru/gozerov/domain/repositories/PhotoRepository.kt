package ru.gozerov.domain.repositories

import android.graphics.Bitmap
import android.graphics.RectF
import ru.gozerov.domain.models.CheckPhotoResult

interface PhotoRepository {

    suspend fun checkPhoto(bitmap: Bitmap, rectF: RectF): CheckPhotoResult

}