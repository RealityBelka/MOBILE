package ru.gozerov.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import ru.gozerov.data.api.PhotoApi
import ru.gozerov.domain.models.CheckPhotoResult
import ru.gozerov.domain.repositories.PhotoRepository
import java.io.File
import java.io.InputStream
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val context: Context,
    private val photoApi: PhotoApi
) : PhotoRepository {

    override suspend fun checkPhoto(bitmap: Bitmap, rectF: RectF): CheckPhotoResult {
        val response = photoApi.checkPhoto(getImagePart())
        return CheckPhotoResult(response.ok, response.message)
    }

    private fun getImagePart(): RequestBody {
        val cacheDir = context.externalCacheDir
        val file = File(cacheDir, "face.jpeg")

        return file.asRequestBody("image/jpeg".toMediaTypeOrNull())
    }

}