package com.katorabian.weatherapp.domain.usecase

import com.katorabian.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend fun fetch(cityId: Int) = repository.getForecast(cityId)
}