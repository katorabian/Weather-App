package com.katorabian.weatherapp.presentation.screen.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.katorabian.weatherapp.presentation.screen.details.DetailsContent
import com.katorabian.weatherapp.presentation.screen.favorite.FavoriteContent
import com.katorabian.weatherapp.presentation.screen.search.SearchContent
import com.katorabian.weatherapp.presentation.ui.theme.WeatherAppTheme

@Composable
fun RootContent(component: RootComponent) {

    WeatherAppTheme {
        Children(
            stack = component.stack
        ) {
            when (val instance: RootComponent.Child = it.instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(component = instance.component)
                }
                is RootComponent.Child.Favorite -> {
                    FavoriteContent(component = instance.component)
                }
                is RootComponent.Child.Search -> {
                    SearchContent(component = instance.component)
                }
            }
        }
    }
}