package com.example.playlistmaker.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesApiService() {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val iTunesApi: ITunesApi by lazy {
        retrofit.create(ITunesApi::class.java)
    }

    companion object {
        private const val BASE_URL = "https://itunes.apple.com/"
        val instance: ITunesApiService by lazy {
            ITunesApiService()
        }
    }
}