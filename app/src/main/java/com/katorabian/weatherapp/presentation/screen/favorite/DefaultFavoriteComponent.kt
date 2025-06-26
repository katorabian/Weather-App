package com.katorabian.weatherapp.presentation.screen.favorite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.katorabian.weatherapp.domain.entity.City
import com.katorabian.weatherapp.presentation.extensions.lazyComponentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultFavoriteComponent @Inject constructor(
    private val favoriteStoreFactory: FavoriteStoreFactory,
    private val onCityItemClick: (City) -> Unit,
    private val onAddToFavoriteClick: () -> Unit,
    private val onSearchClick: () -> Unit,
    componentContext: ComponentContext
) : FavoriteComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { favoriteStoreFactory.create() }
    private val scope by lazyComponentScope()

    init {
        scope.launch {
            store.labels.onEach {
                when (it) {
                    is FavoriteStore.Label.CityItemClick -> onCityItemClick(it.city)
                    FavoriteStore.Label.ClickSearch -> onSearchClick()
                    FavoriteStore.Label.ClickToFavorite -> onAddToFavoriteClick()
                }
            }.collect()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavoriteStore.State> = store.stateFlow

    override fun onClickSearch() {
        store.accept(FavoriteStore.Intent.ClickSearch)
    }

    override fun onClickAddToFavorite() {
        store.accept(FavoriteStore.Intent.ClickAddToFavorite)
    }

    override fun onClickCityItem(city: City) {
        store.accept(FavoriteStore.Intent.CityItemClick(city))
    }
}