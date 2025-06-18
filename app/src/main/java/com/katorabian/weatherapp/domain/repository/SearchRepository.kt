package com.katorabian.weatherapp.domain.repository

import com.katorabian.weatherapp.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>
}