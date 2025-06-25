package com.katorabian.weatherapp.presentation.screen.favorite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.katorabian.weatherapp.domain.entity.City
import com.katorabian.weatherapp.domain.usecase.GetCurrentWeatherUseCase
import com.katorabian.weatherapp.domain.usecase.GetFavoriteCitiesUseCase
import com.katorabian.weatherapp.presentation.screen.favorite.FavoriteStore.Intent
import com.katorabian.weatherapp.presentation.screen.favorite.FavoriteStore.Label
import com.katorabian.weatherapp.presentation.screen.favorite.FavoriteStore.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavoriteStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickSearch: Intent
        data object ClickToFavorite: Intent
        data class CityItemClick(val city: City): Intent
    }

    data class State(
        val cityItems: List<CityItem>
    ) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {
            data object Initial: WeatherState
            data object Loading: WeatherState
            data object Error: WeatherState
            data class Loaded(
                val tempC: Float,
                val iconUrl: String
            ): WeatherState
        }
    }

    sealed interface Label {
        data object ClickSearch: Label
        data object ClickToFavorite: Label
        data class CityItemClick(val city: City): Label
    }
}

class FavoriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavoriteCitesUseCase: GetFavoriteCitiesUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase,
) {

    fun create(): FavoriteStore =
        object : FavoriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavoriteStore",
            initialState = State(listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class FavoriteCitiesLoaded(val cities: List<City>): Action
    }

    private sealed interface Msg {
        data class FavoriteCitiesLoaded(val cities: List<City>): Msg
        data class WeatherLoaded(
            val cityId: Int,
            val tempC: Float,
            val conditionUrl: String
        ): Msg

        data class WeatherLoadingError(
            val cityId: Int
        ): Msg

        data class WeatherIsLoading(
            val cityId: Int
        ): Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavoriteCitesUseCase.get()
                    .onEach {
                        dispatch(Action.FavoriteCitiesLoaded(it))
                    }
                    .collect()
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.CityItemClick -> {
                    publish(
                        Label.CityItemClick(intent.city)
                    )
                }
                Intent.ClickSearch -> {
                    publish(Label.ClickSearch)
                }
                Intent.ClickToFavorite -> {
                    publish(Label.ClickToFavorite)
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.FavoriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavoriteCitiesLoaded(cities))
                    cities.forEach {
                        scope.launch {
                            loadWeatherForCity(it)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                val weather = getCurrentWeatherUseCase.fetch(city.id)
                dispatch(
                    Msg.WeatherLoaded(
                        cityId = city.id,
                        tempC = weather.tempC,
                        conditionUrl = weather.conditionUrl
                    )
                )
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadingError(city.id))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteCitiesLoaded -> {
                copy(
                    cityItems = msg.cities.map {
                        State.CityItem(
                            city = it,
                            weatherState = State.WeatherState.Initial
                        )
                    }
                )
            }
            is Msg.WeatherIsLoading -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy (weatherState = State.WeatherState.Loading)
                        } else {
                            it
                        }
                    }
                )
            }
            is Msg.WeatherLoaded -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.Loaded(
                                    tempC = msg.tempC,
                                    iconUrl = msg.conditionUrl
                                )
                            )
                        } else {
                            it
                        }
                    }
                )
            }
            is Msg.WeatherLoadingError -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(weatherState = State.WeatherState.Error)
                        } else {
                            it
                        }
                    }
                )
            }
        }
    }
}
