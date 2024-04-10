package com.example.playlistmaker.media.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks")
data class TrackEntity(
    @PrimaryKey
    val id: Long,

    @ColumnInfo("track_name")
    val trackName: String,

    @ColumnInfo("artist_name")
    val artistName: String,

    @ColumnInfo("track_time_millis")
    val trackTimeMillis: Long,

    @ColumnInfo("artwork_url")
    val artworkUrl100: String,

    @ColumnInfo("primary_genre_url")
    val primaryGenreName: String?,

    @ColumnInfo("collection_name")
    val collectionName: String,

    @ColumnInfo("country")
    val country: String,

    @ColumnInfo("release_date")
    val releaseDate: String,

    @ColumnInfo("preview_url")
    val previewUrl: String?
)

