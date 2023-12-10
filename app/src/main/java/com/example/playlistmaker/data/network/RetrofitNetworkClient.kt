package com.example.playlistmaker.data.network

import android.util.Log
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient : NetworkClient {
    private val itunesBaseUrl = "https://itunes.apple.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun doRequest(dto: Any): Response {

        return if (dto is TrackSearchRequest) {
            Log.d("RetrofitNetworkClient", dto.toString())
            val response = iTunesService.search(dto.expression).execute()
            Log.d("RetrofitNetworkClient", response.toString())
            val body = response.body() ?: Response()

            body.apply { resultCode = response.code() }
        } else {
            Response().apply { resultCode = 400 }
        }
    }
}