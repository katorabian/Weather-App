package com.katorabian.weatherapp.data.mapper

import com.katorabian.weatherapp.data.network.dto.WeatherCurrentDto
import com.katorabian.weatherapp.data.network.dto.WeatherDto
import com.katorabian.weatherapp.data.network.dto.WeatherForecastDto
import com.katorabian.weatherapp.domain.entity.Forecast
import com.katorabian.weatherapp.domain.entity.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toEntity(): Weather = current.toEntity()

fun WeatherDto.toEntity(): Weather = Weather(
    tempC = tempC,
    conditionText = condition.text,
    conditionUrl = condition.iconUrl.correctImageUrl(),
    date = date.toCalendar()
)

fun WeatherForecastDto.toEntity() = Forecast(
    currentWeather = current.toEntity(),
    upcoming = forecast.forecastDay.drop(1).map { dayDto ->
        val dayWeatherDto = dayDto.dayWeatherDto
        Weather(
            tempC = dayWeatherDto.tempC,
            conditionText = dayWeatherDto.condition.text,
            conditionUrl = dayWeatherDto.condition.iconUrl.correctImageUrl(),
            date = dayDto.date.toCalendar()
        )
    }
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * MILLIS_IN_SECOND)
}

private fun String.correctImageUrl() = "$HTTPS_PROTOCOL$this".replace(
    oldValue = ICON_WEATHER_SIZE_OLD,
    newValue = ICON_WEATHER_SIZE_NEW
)

private const val MILLIS_IN_SECOND = 1000
private const val HTTPS_PROTOCOL = "https:"
private const val ICON_WEATHER_SIZE_OLD = "64x64"
private const val ICON_WEATHER_SIZE_NEW = "128x128"