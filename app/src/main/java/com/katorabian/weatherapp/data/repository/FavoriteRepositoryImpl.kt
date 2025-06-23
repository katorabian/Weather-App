package com.katorabian.weatherapp.data.repository

import com.katorabian.weatherapp.domain.entity.City
import com.katorabian.weatherapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class FavoriteRepositoryImpl : FavoriteRepository {
    override val favoriteCities: Flow<List<City>>
        get() = TODO("Not yet implemented")

    override fun observeIsFavorite(cityId: Int): Flow<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun addToFavorite(city: City) {
        TODO("Not yet implemented")
    }

    override suspend fun removeFromFavorite(cityId: Int) {
        TODO("Not yet implemented")
    }
}