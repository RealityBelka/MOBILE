package ru.gozerov.api.di

import dagger.Binds
import dagger.Module
import ru.gozerov.core.di.FragmentScope
import ru.gozerov.data.repositories.PhotoRepositoryImpl
import ru.gozerov.domain.repositories.PhotoRepository
import javax.inject.Singleton

@Module
interface BiometricBindModule {

    @Binds
    @FragmentScope
    fun bindPhotoRepoImpl(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository

}