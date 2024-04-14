package com.example.playlistmaker.media.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.playlistmaker.search.domain.models.Track

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
) {
    fun mapToDomain(): Track = Track(
        trackId,
        trackTitle,
        artistName,
        trackTime,
        artworkUrl100,
        primaryGenreName,
        collectionName,
        country,
        releaseDate,
        previewUrl,
    )
}
