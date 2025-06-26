package com.katorabian.weatherapp.presentation.screen.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.katorabian.weatherapp.domain.entity.City
import com.katorabian.weatherapp.presentation.screen.details.DefaultDetailsComponent
import com.katorabian.weatherapp.presentation.screen.favorite.DefaultFavoriteComponent
import com.katorabian.weatherapp.presentation.screen.search.DefaultSearchComponent
import com.katorabian.weatherapp.presentation.screen.search.OpenReason
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val favoriteComponentFactory: DefaultFavoriteComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    @Assisted componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    override val stack: Value<ChildStack<*, RootComponent.Child>>
        get() = TODO("Not yet implemented")

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    city = config.city,
                    onBackClick = {},
                    componentContext = componentContext
                )
                RootComponent.Child.Details(component)
            }
            Config.Favorite -> {
                val component = favoriteComponentFactory.create(
                    onCityItemClick = {},
                    onAddToFavoriteClick = {},
                    onSearchClick = {},
                    componentContext = componentContext
                )
                RootComponent.Child.Favorite(component)
            }
            is Config.Search -> {
                val component = searchComponentFactory.create(
                    openReason = config.openReason,
                    onBackClick = {},
                    onSavedToFavorite = {},
                    onForecastRequest = {},
                    componentContext = componentContext
                )
                RootComponent.Child.Search(component)
            }
        }
    }

    sealed interface Config: Parcelable {
        @Parcelize
        data object Favorite: Config

        @Parcelize
        data class Search(val openReason: OpenReason): Config

        @Parcelize
        data class Details(val city: City): Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted componentContext: ComponentContext
        ): DefaultRootComponent
    }
}