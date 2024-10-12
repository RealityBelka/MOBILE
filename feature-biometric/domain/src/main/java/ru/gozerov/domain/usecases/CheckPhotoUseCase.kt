package ru.gozerov.domain.usecases

import android.graphics.Bitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.gozerov.domain.models.CheckPhotoResult
import ru.gozerov.domain.repositories.PhotoRepository
import javax.inject.Inject

class CheckPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {

    suspend operator fun invoke(bitmap: Bitmap) : CheckPhotoResult = withContext(Dispatchers.IO) {
        return@withContext photoRepository.checkPhoto(bitmap)
    }

}