package com.example.playlistmaker.data.dto

data class TrackSearchResponse(
    val resultCount: Int,
    val expression: String,
    val results: List<TrackDto>
) : Response()
