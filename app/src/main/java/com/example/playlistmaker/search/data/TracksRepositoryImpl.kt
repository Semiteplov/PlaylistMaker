package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
) : TracksRepository {
    private var trackForPlaying: Track? = null

    override fun searchTracks(expression: String): Flow<List<Track>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            val tracks = (response as TrackSearchResponse).results.map { trackSearchResult ->
                val formattedReleaseDate = if (trackSearchResult.releaseDate.length >= 4) {
                    trackSearchResult.releaseDate.substring(0, 4)
                } else {
                    trackSearchResult.releaseDate
                }
                val formattedArtworkUrl =
                    trackSearchResult.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

                Track(
                    trackSearchResult.trackId,
                    trackSearchResult.trackTitle,
                    trackSearchResult.artistName,
                    trackSearchResult.trackTime,
                    formattedArtworkUrl,
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

    override fun getTrackForPlaying(): Track? {
        return trackForPlaying?.let {
            val copy = trackForPlaying?.copy()
            trackForPlaying = null
            copy
        }
    }

    override fun saveTrackForPlaying(track: Track?) {
        trackForPlaying = track
    }
}