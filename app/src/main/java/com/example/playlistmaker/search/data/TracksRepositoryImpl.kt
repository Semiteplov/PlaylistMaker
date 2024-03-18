package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
) : TracksRepository {
    private val trackTimeFormatter by lazy {
        SimpleDateFormat("mm:ss", Locale.getDefault())
    }

    override fun searchTracks(expression: String): Flow<List<Track>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            val tracks = (response as TrackSearchResponse).results.map { trackSearchResult ->
                val formattedTrackTime =
                    trackTimeFormatter.format(Date(trackSearchResult.trackTimeMillis))
                val formattedReleaseDate = if (trackSearchResult.releaseDate.length >= 4) {
                    trackSearchResult.releaseDate.substring(0, 4)
                } else {
                    trackSearchResult.releaseDate
                }
                val formattedArtworkUrl =
                    trackSearchResult.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

                Track(
                    trackSearchResult.trackTitle,
                    trackSearchResult.artistName,
                    formattedTrackTime,
                    formattedArtworkUrl,
                    trackSearchResult.trackId,
                    trackSearchResult.primaryGenreName,
                    trackSearchResult.collectionName,
                    trackSearchResult.country,
                    formattedReleaseDate,
                    trackSearchResult.previewUrl,
                )
            }
            emit(tracks)
        } else {
            emit(emptyList())
        }
    }.catch { _ ->
        emit(emptyList())
    }
}