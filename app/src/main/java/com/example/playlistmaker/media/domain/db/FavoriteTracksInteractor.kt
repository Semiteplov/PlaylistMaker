package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    suspend fun addToFavorites(track: Track)
    suspend fun deleteFromFavorites(track: Track)
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun isTrackFavorite(trackId: String): Boolean
    fun saveTrackForPlaying(track: Track)
}