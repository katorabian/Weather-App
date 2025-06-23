package com.katorabian.weatherapp.data.repository

import com.katorabian.weatherapp.domain.entity.Forecast
import com.katorabian.weatherapp.domain.entity.Weather
import com.katorabian.weatherapp.domain.repository.WeatherRepository

class WeatherRepositoryImpl : WeatherRepository {
    override suspend fun getWeather(cityId: Int): Weather {
        TODO("Not yet implemented")
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        TODO("Not yet implemented")
    }
}