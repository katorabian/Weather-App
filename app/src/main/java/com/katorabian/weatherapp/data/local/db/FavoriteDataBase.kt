package com.katorabian.weatherapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.katorabian.weatherapp.data.local.model.CityDbModel

@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class FavoriteDataBase: RoomDatabase() {

    abstract fun favoriteCitiesDao(): FavoriteCitiesDao

    companion object {
        private const val DB_NAME = "FavoriteDataBase"
        private var INSTANCE: FavoriteDataBase? = null
        private val LOCK = Any()

        fun getInstance(context: Context): FavoriteDataBase  {
            INSTANCE?.let { return it }

            synchronized(LOCK) {
                INSTANCE?.let { return it }
                val database = Room.databaseBuilder(
                    context = context,
                    klass = FavoriteDataBase::class.java,
                    name = DB_NAME
                ).build()

                INSTANCE = database
                return database
            }
        }
    }
}