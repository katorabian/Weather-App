package com.katorabian.weatherapp.presentation.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.katorabian.weatherapp.domain.entity.Forecast
import com.katorabian.weatherapp.presentation.ui.theme.CardGradients

@Composable
fun DetailsContent(
    component: DetailsComponent
) {
    val state by component.model.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopBar(
                cityName = state.city.name,
                isCityFavorite = state.isFavorite,
                onBackClick = { component.onClickBack() },
                onClickChangeFavoriteStatus = { component.onClickChangeFavoriteStatus() }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .background(CardGradients.gradients[1].primaryGradient)
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (val forecastState = state.forecastState) {
                DetailsStore.State.ForecastState.Error -> {
                    Error()
                }
                DetailsStore.State.ForecastState.Initial -> {
                    Initial()
                }
                is DetailsStore.State.ForecastState.Loaded -> {
                    Forecast(forecastState.forecast)
                }
                DetailsStore.State.ForecastState.Loading -> {
                    Loading()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    cityName: String,
    isCityFavorite: Boolean,
    onBackClick: () -> Unit,
    onClickChangeFavoriteStatus: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(cityName) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        },
        actions = {
            IconButton(onClick = onClickChangeFavoriteStatus) {
                val icon = if (isCityFavorite) {
                    Icons.Default.Star
                } else {
                    Icons.Default.StarBorder
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    )
}

@Composable
private fun Loading() {

}

@Composable
private fun Forecast(forecast: Forecast) {

}

@Composable
private fun Error() {

}

@Composable
private fun Initial() {

}