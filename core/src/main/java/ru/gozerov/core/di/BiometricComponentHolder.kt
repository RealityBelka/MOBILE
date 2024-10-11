package ru.gozerov.core.di

import android.content.Context
import androidx.fragment.app.Fragment

interface BiometricComponentHolder {

    fun inject(fragment: Fragment)

}

val Context.biometricComponentHolder: BiometricComponentHolder get() = this.applicationContext as BiometricComponentHolder