package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackSearchRequest

interface NetworkClient {
    fun doRequest(dto: TrackSearchRequest): Response
}