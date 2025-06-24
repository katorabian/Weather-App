package com.katorabian.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayDto(
    @SerializedName("data_epoch") val date: Long,
    @SerializedName("day") val dayWeatherDto: DayWeatherDto
)
