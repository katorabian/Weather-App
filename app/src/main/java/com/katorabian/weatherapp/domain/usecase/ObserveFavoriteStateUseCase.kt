package com.katorabian.weatherapp.domain.usecase

import com.katorabian.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class ObserveFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    fun flow(cityId: Int) = repository.observeIsFavorite(cityId)
}