package com.katorabian.weatherapp.di

import android.content.Context
import com.katorabian.weatherapp.data.local.db.FavoriteCitiesDao
import com.katorabian.weatherapp.data.local.db.FavoriteDataBase
import com.katorabian.weatherapp.data.network.api.ApiFactory
import com.katorabian.weatherapp.data.network.api.ApiService
import com.katorabian.weatherapp.data.repository.FavoriteRepositoryImpl
import com.katorabian.weatherapp.data.repository.SearchRepositoryImpl
import com.katorabian.weatherapp.data.repository.WeatherRepositoryImpl
import com.katorabian.weatherapp.domain.repository.FavoriteRepository
import com.katorabian.weatherapp.domain.repository.SearchRepository
import com.katorabian.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindFavoriteRepo(impl: FavoriteRepositoryImpl): FavoriteRepository

    @[ApplicationScope Binds]
    fun bindWeatherRepo(impl: WeatherRepositoryImpl): WeatherRepository

    @[ApplicationScope Binds]
    fun bindSearchRepo(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideApiService(): ApiService = ApiFactory.apiService

        @[ApplicationScope Provides]
        fun provideFavoriteDatabase(context: Context): FavoriteDataBase {
            return FavoriteDataBase.getInstance(context)
        }

        @[ApplicationScope Provides]
        fun provideFavoriteCitiesDao(database: FavoriteDataBase): FavoriteCitiesDao {
            return database.favoriteCitiesDao()
        }
    }
}