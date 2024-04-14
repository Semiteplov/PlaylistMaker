package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.api.FavoriteTracksRepository
import com.example.playlistmaker.media.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTracksInteractorImpl(
    private val repository: FavoriteTracksRepository,
    private val trackRepository: TracksRepository
) :
    FavoriteTracksInteractor {
    override suspend fun addToFavorites(track: Track) {
        repository.addToFavorites(track)
    }

    override suspend fun deleteFromFavorites(track: Track) {
        repository.deleteFromFavorites(track)
    }

    override suspend fun getFavoriteTracks(): Flow<List<Track>> {
        return repository.getFavoriteTracks()
    }

    override suspend fun isTrackFavorite(trackId: String): Boolean {
        return repository.isTrackFavorite(trackId)
    }

    override fun saveTrackForPlaying(track: Track) = trackRepository.saveTrackForPlaying(track)
}