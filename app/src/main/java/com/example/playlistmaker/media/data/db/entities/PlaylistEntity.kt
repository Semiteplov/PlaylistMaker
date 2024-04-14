package com.example.playlistmaker.media.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String?,
    val coverUri: String?,
    val tracksIds: List<Long> = listOf(),
    val tracksCount: Int = 0
)
