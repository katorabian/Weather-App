package com.katorabian.weatherapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
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

        setContent {
            Scaffold { paddingValues ->
                RootContent(
                    modifier = Modifier.padding(paddingValues),
                    component = rootComponentFactory.create(defaultComponentContext())
                )
            }
        }
    }
}