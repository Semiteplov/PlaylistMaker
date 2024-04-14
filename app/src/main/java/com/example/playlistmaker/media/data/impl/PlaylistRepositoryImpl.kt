package com.example.playlistmaker.media.data.impl

import com.example.playlistmaker.media.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.media.data.db.converters.TrackDbConverter
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.dao.TrackDao
import com.example.playlistmaker.media.domain.api.PlaylistRepository
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val trackDao: TrackDao,
    private val playlistConvertor: PlaylistDbConverter,
    private val trackConvertor: TrackDbConverter,
) : PlaylistRepository {

    override suspend fun addPlaylist(playlist: Playlist) {
        playlistDao.addPlaylist(playlistConvertor.map(playlist))
    }

    override fun getPlaylists(): Flow<List<Playlist>> = playlistDao.getPlaylists().map {
        it.map { entity -> playlistConvertor.map(entity) }
    }

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) {
        val tracksIds = playlist.tracksIds.toMutableList()
        tracksIds.add(track.trackId)
        val newPlaylist =
            playlist.copy(tracksIds = tracksIds, tracksCount = playlist.tracksCount + 1)
        playlistDao.updatePlaylist(playlistConvertor.map(newPlaylist))
        trackDao.addTrack(trackConvertor.map(track))
    }
}