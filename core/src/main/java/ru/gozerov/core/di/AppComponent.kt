package ru.gozerov.core.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RetrofitModule::class])
interface AppComponent {

    val retrofit: Retrofit

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

}