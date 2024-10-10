package ru.gozerov.presentation.di

import androidx.lifecycle.Lifecycle
import dagger.BindsInstance
import dagger.Component
import ru.gozerov.core.di.FragmentScope
import ru.gozerov.presentation.screens.voice.VoiceFragment

@FragmentScope
@Component
interface BiometricComponent {

    fun inject(voiceFragment: VoiceFragment)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance lifecycle: Lifecycle
        ): BiometricComponent

    }

}

