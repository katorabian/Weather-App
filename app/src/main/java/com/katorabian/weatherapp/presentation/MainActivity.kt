package com.katorabian.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.katorabian.weatherapp.presentation.screen.root.DefaultRootComponent
import com.katorabian.weatherapp.presentation.screen.root.RootContent
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            RootContent(component = rootComponentFactory.create(defaultComponentContext()))
        }
    }
}