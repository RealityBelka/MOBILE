package ru.gozerov.data.repositories

import android.graphics.Bitmap
import android.graphics.RectF
import ru.gozerov.domain.models.CheckPhotoResult
import ru.gozerov.domain.repositories.PhotoRepository
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor() : PhotoRepository {

    override suspend fun checkPhoto(bitmap: Bitmap, rectF: RectF): CheckPhotoResult {
        return CheckPhotoResult(true)
    }

}