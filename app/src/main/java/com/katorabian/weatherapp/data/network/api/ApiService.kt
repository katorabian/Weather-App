package com.katorabian.weatherapp.data.network.api

import com.katorabian.weatherapp.data.network.dto.CityDto
import com.katorabian.weatherapp.data.network.dto.WeatherCurrentDto
import com.katorabian.weatherapp.data.network.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("current.json?key=$apiKey")
    suspend fun loadCurrentWeather(
        @Query("q") query: String
    ): WeatherCurrentDto

    @GET("forecast.json?key=$apiKey")
    suspend fun loadForecast(
        @Query("q") query: String,
        @Query("days") daysCount: Int = 4
    ): WeatherForecastDto

    @GET("search.json?key=$apiKey")
    suspend fun searchCities(
        @Query("q") query: String
    ): List<CityDto>


}