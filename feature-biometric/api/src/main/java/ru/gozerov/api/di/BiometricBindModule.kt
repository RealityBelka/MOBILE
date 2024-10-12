package ru.gozerov.api.di

import dagger.Binds
import dagger.Module
import ru.gozerov.core.di.FragmentScope
import ru.gozerov.data.repositories.PhotoRepositoryImpl
import ru.gozerov.data.repositories.RuleRepositoryImpl
import ru.gozerov.data.repositories.VoiceRepositoryImpl
import ru.gozerov.domain.repositories.PhotoRepository
import ru.gozerov.domain.repositories.RuleRepository
import ru.gozerov.domain.repositories.VoiceRepository

@Module
interface BiometricBindModule {

    @Binds
    @FragmentScope
    fun bindRuleRepoImpl(ruleRepositoryImpl: RuleRepositoryImpl): RuleRepository

    @Binds
    @FragmentScope
    fun bindPhotoRepoImpl(photoRepositoryImpl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    @FragmentScope
    fun bindVoiceRepoImpl(voiceRepositoryImpl: VoiceRepositoryImpl): VoiceRepository

}