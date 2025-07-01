package com.katorabian.weatherapp.data.network.api

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import com.katorabian.weatherapp.BuildConfig
import com.katorabian.weatherapp.di.ApplicationScope
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Inject

@ApplicationScope
class ApiFactory @Inject constructor(context: Context) {
    val lang = Locale.current.language
        .takeIf { it == LANG_VALUE_RU } ?: LANG_VALUE_DEF

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val origRequest = chain.request()
            val newUrl = origRequest.url().newBuilder()
                .addQueryParameter(KEY_PARAM, BuildConfig.WEATHER_API_KEY)
                .addQueryParameter(LANG_PARAM, lang)
                .build()
            val newRequest = origRequest.newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }.build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create()

    companion object {
        private const val BASE_URL = "https://api.weatherapi.com/v1/"
        private const val KEY_PARAM = "key"
        private const val LANG_PARAM = "lang"
        private const val LANG_VALUE_RU = "ru"
        private const val LANG_VALUE_DEF = "en"
    }
}