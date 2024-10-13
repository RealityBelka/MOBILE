package ru.gozerov.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import androidx.core.graphics.toRect
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import ru.gozerov.data.api.PhotoApi
import ru.gozerov.data.models.HintResponse
import ru.gozerov.data.utils.parseError
import ru.gozerov.domain.models.CheckPhotoResult
import ru.gozerov.domain.repositories.PhotoRepository
import java.io.File
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val context: Context,
    private val photoApi: PhotoApi,
    private val retrofit: Retrofit
) : PhotoRepository {

    override suspend fun checkPhoto(bitmap: Bitmap, rectF: RectF): CheckPhotoResult {
        val rectInt = rectF.toRect()
        val rectString = listOf(
            rectInt.left,
            rectInt.right,
            rectInt.top,
            rectInt.bottom
        ).joinToString(separator = " ")
        val numbers = rectString.toRequestBody("text/plain".toMediaTypeOrNull())

        val response = photoApi.checkPhoto(getImagePart(), numbers)

        if (!response.isSuccessful) {
            val errorResponse = parseError(response, retrofit)
            if (errorResponse != null) {
                return CheckPhotoResult(errorResponse.ok, errorResponse.message)
            }
        } else {
            val hintResponse = response.body()
            hintResponse?.let { hint ->
                return CheckPhotoResult(hint.ok, hint.message)
            }
        }
        return CheckPhotoResult(false, null)
    }

    private fun getImagePart(): MultipartBody.Part? {
        val cacheDir = context.externalCacheDir ?: return null
        val file = File(cacheDir, "face.jpeg")

        if (!file.exists()) {
            return null
        }
        val mimeType = "image/jpeg"

        val requestBody: RequestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
        val part: MultipartBody.Part =
            MultipartBody.Part.createFormData("photo", file.name, requestBody)

        return part
    }


    /*
    *   val cacheDir = context.externalCacheDir
        val file = File(cacheDir, "face.jpeg")

        return file.asRequestBody("image/jpeg".toMediaTypeOrNull())*/


}