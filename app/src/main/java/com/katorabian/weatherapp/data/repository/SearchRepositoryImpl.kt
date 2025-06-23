package com.katorabian.weatherapp.data.repository

import com.katorabian.weatherapp.domain.entity.City
import com.katorabian.weatherapp.domain.repository.SearchRepository

class SearchRepositoryImpl : SearchRepository {
    override suspend fun search(query: String): List<City> {
        TODO("Not yet implemented")
    }
}