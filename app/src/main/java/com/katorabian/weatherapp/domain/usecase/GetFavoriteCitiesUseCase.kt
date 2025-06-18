package com.katorabian.weatherapp.domain.usecase

import com.katorabian.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    fun get() = repository.favoriteCities
}