package com.example.playlistmaker.media.domain.models

data class Playlist(
    val id: String,
    val name: String,
    val description: String?,
    val coverUri: String?,
    val tracksIds: List<Long> = listOf(),
    val tracksCount: Int = 0
)
