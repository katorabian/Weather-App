package com.katorabian.weatherapp.data.mapper

import com.katorabian.weatherapp.data.network.dto.CityDto
import com.katorabian.weatherapp.domain.entity.City

fun CityDto.toEntity(): City = City(id, name, country)

fun List<CityDto>.toEntities(): List<City> = map { it.toEntity() }