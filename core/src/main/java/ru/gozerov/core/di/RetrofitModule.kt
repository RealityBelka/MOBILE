package ru.gozerov.core.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.gozerov.core.domain.NetworkUtils
import javax.inject.Singleton

@Module
interface RetrofitModule {

    companion object {

        @Singleton
        @Provides
        fun provideRetrofit(): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .readTimeout(20000L, java.util.concurrent.TimeUnit.MILLISECONDS)
                .writeTimeout(20000L, java.util.concurrent.TimeUnit.MILLISECONDS)
                .build()

            return Retrofit
                .Builder()
                .baseUrl(NetworkUtils.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
                .build()

        }

    }
}