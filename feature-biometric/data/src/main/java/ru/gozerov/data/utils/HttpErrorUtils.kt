package ru.gozerov.data.utils

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import ru.gozerov.data.models.HintResponse

fun parseError(response: Response<HintResponse>, retrofit: Retrofit): HintResponse? {
    val converter: Converter<ResponseBody, HintResponse> = retrofit
        .responseBodyConverter(HintResponse::class.java, arrayOfNulls(0))

    response.errorBody()?.let { errorBody ->
        return try {
            converter.convert(errorBody)
        } catch (e: Exception) {
            null
        }
    }
    return null
}