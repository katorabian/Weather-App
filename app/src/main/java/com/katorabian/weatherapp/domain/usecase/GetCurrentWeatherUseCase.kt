package com.katorabian.weatherapp.domain.usecase

import com.katorabian.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend fun fetch(cityId: Int) = repository.getWeather(cityId)
}