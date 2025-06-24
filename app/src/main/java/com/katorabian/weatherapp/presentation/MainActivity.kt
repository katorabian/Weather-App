package com.katorabian.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.katorabian.weatherapp.data.network.api.ApiFactory
import com.katorabian.weatherapp.presentation.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = ApiFactory.apiService
        CoroutineScope(Dispatchers.Main).launch {
            val currentWeather = apiService.loadCurrentWeather("London")
            val forecast = apiService.searchCities("London")
            val cities = apiService.searchCities("London")
            Log.d(
                this@MainActivity::class.java.simpleName,
                "Current Weather: $currentWeather\n" +
                        "Forecast weather: ${forecast.joinToString()}\n" +
                        "Cities: ${cities.joinToString()}"
            )
        }

        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {

            }
        }
    }
}