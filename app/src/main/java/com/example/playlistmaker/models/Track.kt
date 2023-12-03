package com.example.playlistmaker.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class Track(
    @SerializedName("trackName")
    val trackTitle: String,

    @SerializedName("artistName")
    val artistName: String,

    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long,

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
) {
    fun getFormattedTime(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackTimeMillis)
    }

    fun getYearFromReleaseDate(): String = releaseDate.substring(0, 4)

    fun getImageNeedSize(): String = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
}