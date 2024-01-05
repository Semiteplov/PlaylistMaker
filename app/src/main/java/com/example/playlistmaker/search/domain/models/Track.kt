package com.example.playlistmaker.search.domain.models

data class Track(
    val trackTitle: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val trackId: Long,
    val primaryGenreName: String,
    val collectionName: String,
    val country: String,
    val releaseDate: String,
    val previewUrl: String?,
)