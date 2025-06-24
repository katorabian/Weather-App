package com.katorabian.weatherapp.domain.usecase

import com.katorabian.weatherapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
) {

    suspend fun search(query: String) = repository.search(query)

}