package com.katorabian.weatherapp.presentation.screen.details

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

class DefaultDetailsComponent @AssistedInject constructor(
    private val detailsStoreFactory: DetailsStoreFactory,
    @Assisted("city") private val city: City,
    @Assisted("onBackClick") private val onBackClick: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { detailsStoreFactory.create(city) }
    private val scope by lazyComponentScope()

    init {
        scope.launch {
            store.labels.onEach {
                when (it) {
                    DetailsStore.Label.ClickBack -> onBackClick()
                }
            }.collect()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(DetailsStore.Intent.ClickBack)
    }

    override fun onClickChangeFavoriteStatus() {
        store.accept(DetailsStore.Intent.ClickChangeFavoriteStatus)
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("city") city: City,
            @Assisted("onBackClick") onBackClick: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ): DefaultDetailsComponent
    }
}