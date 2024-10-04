package ru.gozerov.api.di

import dagger.Component
import ru.gozerov.core.di.AppComponent

@Component(dependencies = [AppComponent::class])
interface BiometricComponent