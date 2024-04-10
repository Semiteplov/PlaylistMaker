package com.example.playlistmaker.media.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class FavoriteTrackEntity(
    @PrimaryKey
    val trackId: Long,
    val trackTitle: String,
    val artistName: String,
    val trackTime: Long,
    val artworkUrl100: String,
    val primaryGenreName: String?,
    val collectionName: String,
    val country: String,
    val releaseDate: String,
    val previewUrl: String?,
    val addedTime: Long? = null,
)
