package com.katorabian.weatherapp.presentation

import android.app.Application
import com.katorabian.weatherapp.di.ApplicationComponent
import com.katorabian.weatherapp.di.DaggerApplicationComponent

class WeatherApp: Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.factory()
            .create(this)
    }
}