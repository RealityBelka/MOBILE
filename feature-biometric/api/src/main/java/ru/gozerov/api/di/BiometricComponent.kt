package ru.gozerov.api.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.core.di.AppComponent
import ru.gozerov.core.di.FragmentScope
import ru.gozerov.presentation.screens.face_capturing.FaceCapturingFragment
import ru.gozerov.presentation.screens.face_rules_list.FaceRulesListFragment
import ru.gozerov.presentation.screens.recording_list.RecordingListFragment
import ru.gozerov.presentation.screens.voice.VoiceFragment

@FragmentScope
@Component(
    modules = [BiometricModule::class, BiometricApiModule::class],
    dependencies = [AppComponent::class]
)
interface BiometricComponent {

    fun inject(voiceFragment: VoiceFragment)

    fun inject(faceCapturingFragment: FaceCapturingFragment)

    fun inject(faceRulesListFragment: FaceRulesListFragment)

    fun inject(recordingListFragment: RecordingListFragment)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context, appComponent: AppComponent): BiometricComponent

    }

}