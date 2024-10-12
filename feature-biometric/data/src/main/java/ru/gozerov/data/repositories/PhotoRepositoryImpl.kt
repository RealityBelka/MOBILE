package ru.gozerov.data.repositories

import android.graphics.Bitmap
import ru.gozerov.domain.models.CheckPhotoResult
import ru.gozerov.domain.repositories.PhotoRepository
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor() : PhotoRepository {

    override suspend fun checkPhoto(bitmap: Bitmap): CheckPhotoResult {
        return CheckPhotoResult(true)
    }

}