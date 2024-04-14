package com.example.playlistmaker.media.data.impl

import android.util.Log
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.converters.FavoriteTrackDbConvertor
import com.example.playlistmaker.media.domain.api.FavoriteTracksRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
class FavoriteTracksRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val convertor: FavoriteTrackDbConvertor,
) : FavoriteTracksRepository {
    override suspend fun addToFavorites(track: Track) {
        appDatabase.favoriteTrackDao().insert(convertor.map(track))
    }

    override suspend fun deleteFromFavorites(track: Track) {
        appDatabase.favoriteTrackDao().delete(convertor.map(track))
    }

    override suspend fun getFavoriteTracks(): Flow<List<Track>> {
        return appDatabase.favoriteTrackDao().getAll().map {
            it.map { entity -> entity.mapToDomain() }
        }
    }

    override suspend fun isTrackFavorite(trackId: String): Boolean {
        return appDatabase.favoriteTrackDao().isTrackExist(trackId)
    }
}

