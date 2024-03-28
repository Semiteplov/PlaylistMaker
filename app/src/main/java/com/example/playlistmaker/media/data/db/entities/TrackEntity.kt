package com.example.playlistmaker.media.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
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
    val addedTime: Long?
)
