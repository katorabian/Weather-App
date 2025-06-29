package com.katorabian.weatherapp.presentation.screen.favorite

import com.katorabian.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavoriteComponent {

    val model: StateFlow<FavoriteStore.State>

    fun onClickSearch()
    fun onClickAddToFavorite()
    fun onClickCityItem(city: City)
}