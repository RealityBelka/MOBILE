package ru.gozerov.data.repositories

import ru.gozerov.domain.models.CheckPhotoResult
import ru.gozerov.domain.repositories.PhotoRepository
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(): PhotoRepository {

    override suspend fun checkPhoto(): CheckPhotoResult {
        return CheckPhotoResult(true)
    }

}