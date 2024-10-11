package ru.gozerov.domain.repositories

import ru.gozerov.domain.models.CheckPhotoResult

interface PhotoRepository {

    suspend fun checkPhoto(): CheckPhotoResult

}