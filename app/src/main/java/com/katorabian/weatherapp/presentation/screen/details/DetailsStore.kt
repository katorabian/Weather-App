package com.katorabian.weatherapp.presentation.screen.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.katorabian.weatherapp.domain.entity.City
import com.katorabian.weatherapp.domain.entity.Forecast
import com.katorabian.weatherapp.domain.usecase.ChangeFavoriteStateUseCase
import com.katorabian.weatherapp.domain.usecase.GetForecastUseCase
import com.katorabian.weatherapp.domain.usecase.ObserveFavoriteStateUseCase
import com.katorabian.weatherapp.presentation.screen.details.DetailsStore.Intent
import com.katorabian.weatherapp.presentation.screen.details.DetailsStore.Label
import com.katorabian.weatherapp.presentation.screen.details.DetailsStore.State
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object ClickBack: Intent
        data object ClickChangeFavoriteStatus: Intent
    }

    data class State(
        val city: City,
        val isFavorite: Boolean,
        val forecastState: ForecastState
    ) {
        sealed interface ForecastState {
            data object Initial: ForecastState
            data object Loading: ForecastState
            data object Error: ForecastState
            data class Loaded(
                val forecast: Forecast
            ): ForecastState
        }
    }

    sealed interface Label {
        data object ClickBack: Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase,
    private val observeFavoriteStateUseCase: ObserveFavoriteStateUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                city = city,
                isFavorite = false,
                forecastState = State.ForecastState.Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class FavoriteStatusChanged(val isFavorite: Boolean): Action
        data class ForecastLoaded(val forecast: Forecast): Action
        data object ForecastLoadingStart: Action
        data object ForecastLoadingError: Action
    }

    private sealed interface Msg {
        data class FavoriteStatusChanged(val isFavorite: Boolean): Msg
        data class ForecastLoaded(val forecast: Forecast): Msg
        data object ForecastLoadingStart: Msg
        data object ForecastLoadingError: Msg
    }

    private inner class BootstrapperImpl(
        private val city: City
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeFavoriteStateUseCase.flow(cityId = city.id)
                    .onEach { isFavorite ->
                        dispatch(Action.FavoriteStatusChanged(isFavorite))
                    }.collect()
            }
            scope.launch {
                dispatch(Action.ForecastLoadingStart)
                try {
                    val forecast = getForecastUseCase.fetch(cityId = city.id)
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }
                Intent.ClickChangeFavoriteStatus -> scope.launch {
                    val state = getState()
                    if (state.isFavorite) {
                        changeFavoriteStateUseCase.removeFrom(state.city.id)
                    } else {
                        changeFavoriteStateUseCase.addTo(state.city)
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavoriteStatusChanged -> {
                    dispatch(Msg.FavoriteStatusChanged(action.isFavorite))
                }
                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }
                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }
                Action.ForecastLoadingStart -> {
                    dispatch(Msg.ForecastLoadingStart)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteStatusChanged -> copy(
                isFavorite = msg.isFavorite
            )
            is Msg.ForecastLoaded -> copy(
                forecastState = State.ForecastState.Loaded(msg.forecast)
            )
            Msg.ForecastLoadingError -> copy(
                forecastState = State.ForecastState.Error
            )
            Msg.ForecastLoadingStart -> copy(
                forecastState = State.ForecastState.Loading
            )
        }
    }
}
