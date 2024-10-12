package ru.gozerov.api.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import ru.gozerov.data.api.PhotoApi
import ru.gozerov.data.api.VoiceApi

@Module
interface BiometricApiModule {

    companion object {

        @Provides
        fun providePhotoApi(retrofit: Retrofit): PhotoApi = retrofit.create(PhotoApi::class.java)

        @Provides
        fun provideVoiceApi(retrofit: Retrofit): VoiceApi = retrofit.create(VoiceApi::class.java)

    }

}