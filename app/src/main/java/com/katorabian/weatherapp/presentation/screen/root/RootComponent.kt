package com.katorabian.weatherapp.presentation.screen.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.katorabian.weatherapp.presentation.screen.details.DetailsComponent
import com.katorabian.weatherapp.presentation.screen.favorite.FavoriteComponent
import com.katorabian.weatherapp.presentation.screen.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Favorite(val component: FavoriteComponent): Child
        data class Search(val component: SearchComponent): Child
        data class Details(val component: DetailsComponent): Child
    }
}