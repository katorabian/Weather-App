package com.katorabian.weatherapp.presentation.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.katorabian.weatherapp.R
import com.katorabian.weatherapp.domain.entity.Forecast
import com.katorabian.weatherapp.domain.entity.Weather
import com.katorabian.weatherapp.presentation.extensions.formattedFullDate
import com.katorabian.weatherapp.presentation.extensions.formattedShortDayOfWeek
import com.katorabian.weatherapp.presentation.extensions.tempToFormatString
import com.katorabian.weatherapp.presentation.ui.theme.CardGradients

@Composable
fun DetailsContent(
    component: DetailsComponent
) {
    val state by component.model.collectAsState()

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.background,
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Forecast(forecast: Forecast) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1F))
        Text(
            text = forecast.currentWeather.conditionText,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = forecast.currentWeather.tempC.tempToFormatString(),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 70.sp)
            )
            GlideImage(
                modifier = Modifier.size(70.dp),
                model = forecast.currentWeather.conditionUrl,
                contentDescription = null
            )
        }
        Text(
            text = forecast.currentWeather.date.formattedFullDate(),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.weight(1F))
        UpcomingWeather(upcoming = forecast.upcoming)
        Spacer(modifier = Modifier.weight(0.5F))
    }
}

@Composable
private fun UpcomingWeather(upcoming: List<Weather>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(
                alpha = 0.3F
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Text(
                text = stringResource(R.string.upcoming),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                upcoming.forEach {
                    SmallWeatherCard(weather = it)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RowScope.SmallWeatherCard(weather: Weather) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        modifier = Modifier
            .height(128.dp)
            .weight(1F)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = weather.tempC.tempToFormatString())
            GlideImage(
                model = weather.conditionUrl,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Text(text = weather.date.formattedShortDayOfWeek())
        }
    }
}

@Composable
private fun Error() {

}

@Composable
private fun Initial() {

}