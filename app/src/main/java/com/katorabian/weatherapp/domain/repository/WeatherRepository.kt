package com.katorabian.weatherapp.domain.repository

import com.katorabian.weatherapp.domain.entity.City
import com.katorabian.weatherapp.domain.entity.Forecast
import com.katorabian.weatherapp.domain.entity.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    val favoriteCities: Flow<City>

    fun observeIsFavorite(cityId: Int): Flow<Boolean>

    suspend fun addToFavorite(city: City)
    suspend fun removeFromFavorite(cityId: Int)

    suspend fun getWeather(cityId: Int): Weather
    suspend fun getForecast(cityId: Int): Forecast

    suspend fun search(query: String): List<City>
}