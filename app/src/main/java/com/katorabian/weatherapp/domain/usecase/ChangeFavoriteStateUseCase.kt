package com.katorabian.weatherapp.domain.usecase

import com.katorabian.weatherapp.domain.entity.City
import com.katorabian.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class ChangeFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    suspend fun addTo(city: City) = repository.addToFavorite(city)

    suspend fun removeFrom(cityId: Int) = repository.removeFromFavorite(cityId)
}