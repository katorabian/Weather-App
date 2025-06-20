package com.katorabian.weatherapp.data.network.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiFactory {

    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val origRequest = chain.request()
            val newUrl = origRequest.url().newBuilder()
                .addQueryParameter("key", "<key>")
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
}