package com.katorabian.weatherapp.presentation.screen.details

import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    val model: StateFlow<DetailsStore.State>

    fun onClickBack()
    fun onClickChangeFavoriteStatus()
}