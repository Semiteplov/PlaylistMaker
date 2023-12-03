package com.example.playlistmaker.network

import com.example.playlistmaker.models.Track

data class ITunesResponse(
    val resultCount: Int,
    val results: List<Track>
)
