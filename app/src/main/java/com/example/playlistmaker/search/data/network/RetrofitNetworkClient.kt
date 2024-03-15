package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitNetworkClient(private val iTunesService: ITunesApi) : NetworkClient {
    override suspend fun doRequest(dto: TrackSearchRequest): Response {
        val response = iTunesService.search(dto.expression)
        val body = response.body() ?: Response()

        body.apply { resultCode = response.code() }
        return body
    }
}