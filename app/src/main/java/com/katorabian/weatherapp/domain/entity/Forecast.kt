package com.katorabian.weatherapp.domain.entity

class Forecast(
    val currentWeather: Weather,
    val upcoming: List<Weather>
)