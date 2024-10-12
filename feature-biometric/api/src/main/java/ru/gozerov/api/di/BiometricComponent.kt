package ru.gozerov.api.di

import dagger.Component
import ru.gozerov.core.di.BiometricComponentHolder
import ru.gozerov.core.di.FragmentScope
import ru.gozerov.core.navigation.Screen
import ru.gozerov.presentation.screens.face_capturing.FaceCapturingFragment
import ru.gozerov.presentation.screens.face_rules_list.FaceRulesListFragment
import ru.gozerov.presentation.screens.recording_list.RecordingListFragment
import ru.gozerov.presentation.screens.voice.VoiceFragment

@FragmentScope
@Component(modules = [BiometricModule::class])
interface BiometricComponent {

    fun inject(voiceFragment: VoiceFragment)

    fun inject(faceCapturingFragment: FaceCapturingFragment)

    fun inject(faceRulesListFragment: FaceRulesListFragment)

    fun inject(recordingListFragment: RecordingListFragment)

}