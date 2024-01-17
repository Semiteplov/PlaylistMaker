package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val iTunesService: ITunesApi) : NetworkClient {
    override fun doRequest(dto: TrackSearchRequest): Response {
        val response = iTunesService.search(dto.expression).execute()
        val body = response.body() ?: Response()

        body.apply { resultCode = response.code() }
        return body
    }
}