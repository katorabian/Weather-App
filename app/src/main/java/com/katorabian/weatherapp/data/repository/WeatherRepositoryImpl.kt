package com.katorabian.weatherapp.data.repository

import com.katorabian.weatherapp.data.mapper.toEntity
import com.katorabian.weatherapp.data.network.api.ApiService
import com.katorabian.weatherapp.domain.entity.Forecast
import com.katorabian.weatherapp.domain.entity.Weather
import com.katorabian.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WeatherRepository {
    override suspend fun getWeather(cityId: Int): Weather {
        val query = "$PREFIX_CITY_ID$cityId"
        return apiService.loadCurrentWeather(query).toEntity()
    }

    override suspend fun getForecast(cityId: Int): Forecast {
        val query = "$PREFIX_CITY_ID$cityId"
        return apiService.loadForecast(query).toEntity()
    }

    companion object {
        private const val PREFIX_CITY_ID = "id:"
    }
}