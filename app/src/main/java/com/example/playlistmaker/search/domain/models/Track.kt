package com.example.playlistmaker.search.domain.models

data class Track(
    val trackId: Long,
    val trackTitle: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val primaryGenreName: String?,
    val collectionName: String,
    val country: String,
    val releaseDate: String,
    val previewUrl: String?,
    var isFavorite: Boolean = false,
    var addedTime: Long? = null,
)