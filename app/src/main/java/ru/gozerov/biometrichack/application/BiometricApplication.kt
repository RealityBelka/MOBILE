package ru.gozerov.biometrichack.application

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import ru.gozerov.api.di.BiometricComponent
import ru.gozerov.api.di.DaggerBiometricComponent
import ru.gozerov.core.di.AppComponent
import ru.gozerov.core.di.BiometricComponentHolder
import ru.gozerov.core.di.DaggerAppComponent
import ru.gozerov.core.navigation.Screen
import ru.gozerov.presentation.screens.face_capturing.FaceCapturingFragment
import ru.gozerov.presentation.screens.face_rules_list.FaceRulesListFragment
import ru.gozerov.presentation.screens.recording_list.RecordingListFragment
import ru.gozerov.presentation.screens.voice.VoiceFragment

class BiometricApplication: Application(), BiometricComponentHolder {

    val applicationComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    private var biometricComponent: BiometricComponent? = null

    override fun inject(fragment: Fragment) {
        if (biometricComponent == null)
            biometricComponent = DaggerBiometricComponent.create()

        when(fragment) {
            is FaceCapturingFragment -> biometricComponent?.inject(fragment)
            is VoiceFragment -> biometricComponent?.inject(fragment)
            is FaceRulesListFragment -> biometricComponent?.inject(fragment)
            is RecordingListFragment -> biometricComponent?.inject(fragment)
        }
    }

}


val Context.appComponent
    get() = (applicationContext as BiometricApplication).applicationComponent
