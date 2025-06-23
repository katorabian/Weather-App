package com.katorabian.weatherapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [DataModule::class]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ): ApplicationComponent
    }
}