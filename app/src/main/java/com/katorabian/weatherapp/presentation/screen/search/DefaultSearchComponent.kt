package com.katorabian.weatherapp.presentation.screen.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.katorabian.weatherapp.domain.entity.City
import com.katorabian.weatherapp.presentation.extensions.lazyComponentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultSearchComponent @AssistedInject constructor(
    private val searchStoreFactory: SearchStoreFactory,
    @Assisted("openReason") private val openReason: OpenReason,
    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("onSavedToFavorite") private val onSavedToFavorite: () -> Unit,
    @Assisted("onForecastRequest") private val onForecastRequest: (city: City) -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { searchStoreFactory.create(openReason) }
    private val scope by lazyComponentScope()

    init {
        scope.launch {
            store.labels.onEach {
                when (it) {
                    SearchStore.Label.ClickBack -> onBackClick()
                    is SearchStore.Label.OpenForecast -> onForecastRequest(it.city)
                    SearchStore.Label.SavedToFavorite -> onSavedToFavorite()
                }
            }.collect()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun changeSearchQuery(query: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
    }

    override fun onClickBack() {
        store.accept(SearchStore.Intent.ClickBack)
    }

    override fun onClickSearch() {
        store.accept(SearchStore.Intent.ClickSearch)
    }

    override fun onClickCity(city: City) {
        store.accept(SearchStore.Intent.ClickCity(city))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("openReason") openReason: OpenReason,
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("onSavedToFavorite") onSavedToFavorite: () -> Unit,
            @Assisted("onForecastRequest") onForecastRequest: (city: City) -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultSearchComponent
    }
}