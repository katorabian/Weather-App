package com.katorabian.weatherapp.presentation.screen.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.pushNew
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
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializerOrNull

class DefaultRootComponent @AssistedInject constructor(
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val favoriteComponentFactory: DefaultFavoriteComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    @Assisted componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    @OptIn(InternalSerializationApi::class)
    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Favorite,
        handleBackButton = true,
        childFactory = ::child,
        serializer = Config::class.serializerOrNull()
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when (config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    city = config.city,
                    onBackClick = {
                        navigation.pop()
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Details(component)
            }
            Config.Favorite -> {
                val component = favoriteComponentFactory.create(
                    onCityItemClick = {
                        navigation.pushNew(Config.Details(it))
                    },
                    onAddToFavoriteClick = {
                        navigation.pushNew(Config.Search(OpenReason.AddToFavorite))
                    },
                    onSearchClick = {
                        navigation.pushNew(Config.Search(OpenReason.RegularSearch))
                    },
                    componentContext = componentContext
                )
                RootComponent.Child.Favorite(component)
            }
            is Config.Search -> {
                val component = searchComponentFactory.create(
                    openReason = config.openReason,
                    onBackClick = {
                        navigation.pop()
                    },
                    onSavedToFavorite = {
                        navigation.pop()
                    },
                    onForecastRequest = {
                        navigation.pushNew(Config.Details(it))
                    },
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