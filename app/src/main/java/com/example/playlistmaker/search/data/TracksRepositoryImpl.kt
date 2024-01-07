package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
) : TracksRepository {
    private val trackTimeFormatter by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackSearchResponse).results.map {
                val formattedTrackTime =
                    trackTimeFormatter.format(it.trackTimeMillis)
                val formattedReleaseDate = if (it.releaseDate.length >= 4) {
                    it.releaseDate.substring(0, 4)
                } else {
                    it.releaseDate
                }
                val formattedArtworkUrl = it.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

                Track(
                    it.trackTitle,
                    it.artistName,
                    formattedTrackTime,
                    formattedArtworkUrl,
                    it.trackId,
                    it.primaryGenreName,
                    it.collectionName,
                    it.country,
                    formattedReleaseDate,
                    it.previewUrl,
                )
            }
        } else {
            return emptyList()
        }
    }
}