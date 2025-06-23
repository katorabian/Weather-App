package com.katorabian.weatherapp.data.repository

import com.katorabian.weatherapp.data.mapper.toEntities
import com.katorabian.weatherapp.data.network.api.ApiService
import com.katorabian.weatherapp.domain.entity.City
import com.katorabian.weatherapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {

    override suspend fun search(query: String): List<City> {
        return apiService.searchCities(query).toEntities()
    }
}