package com.example.playlistmaker.search.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    @SerializedName("trackName")
    val trackTitle: String,

    @SerializedName("artistName")
    val artistName: String,

    @SerializedName("trackTimeMillis")
    val trackTime: Long,

    @SerializedName("artworkUrl100")
    val artworkUrl100: String,

    @SerializedName("trackId")
    val trackId: Long,

    @SerializedName("primaryGenreName")
    val primaryGenreName: String,

    @SerializedName("collectionName")
    val collectionName: String,

    @SerializedName("country")
    val country: String,

    @SerializedName("releaseDate")
    val releaseDate: String,

    @SerializedName("previewUrl")
    val previewUrl: String?,
)