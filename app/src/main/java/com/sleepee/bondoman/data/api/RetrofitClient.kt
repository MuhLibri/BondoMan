package com.sleepee.bondoman.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://pbd-backend-2024.vercel.app"
    private var instance: Retrofit? = null

    val Instance : Retrofit
        get() {
            if (instance == null) {
                instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return instance!!
        }
}