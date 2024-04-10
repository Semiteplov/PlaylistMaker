package com.example.playlistmaker.media.domain.impl

import android.net.Uri
import com.example.playlistmaker.media.domain.api.ExternalStorageRepository
import com.example.playlistmaker.media.domain.api.PlaylistRepository
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class PlaylistInteractorImpl(
    private val externalStorageRepository: ExternalStorageRepository,
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun addPlaylist(name: String, description: String?, coverUri: Uri?) {
        val id = UUID.randomUUID().toString()
        val playlistCoverUri = coverUri?.let {
            externalStorageRepository.savePlaylistCover(id, coverUri)
        }
        val playlist = Playlist(
            id = id,
            name = name,
            description = description,
            coverUri = playlistCoverUri
        )
        playlistRepository.addPlaylist(playlist)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = playlistRepository.getPlaylists()

    override suspend fun updatePlaylist(playlist: Playlist, track: Track) {
        playlistRepository.updatePlaylist(playlist, track)
    }
}