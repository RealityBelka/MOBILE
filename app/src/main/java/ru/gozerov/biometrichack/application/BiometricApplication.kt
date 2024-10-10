package ru.gozerov.biometrichack.application

import android.app.Application
import android.content.Context
import ru.gozerov.core.di.AppComponent
import ru.gozerov.core.di.DaggerAppComponent

class BiometricApplication: Application() {

    val applicationComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}


val Context.appComponent
    get() = (applicationContext as BiometricApplication).applicationComponent
