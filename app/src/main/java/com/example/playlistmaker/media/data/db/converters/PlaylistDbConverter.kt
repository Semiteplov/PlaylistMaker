package com.example.playlistmaker.media.data.db.converters

import com.example.playlistmaker.media.data.db.entities.PlaylistEntity
import com.example.playlistmaker.media.domain.models.Playlist

class PlaylistDbConverter {
    fun map(playlist: Playlist) = PlaylistEntity(
        id = playlist.id,
        name = playlist.name,
        description = playlist.description,
        coverUri = playlist.coverUri,
        tracksIds = playlist.tracksIds,
        tracksCount = playlist.tracksCount
    )

    fun map(playlistEntity: PlaylistEntity): Playlist = Playlist(
        id = playlistEntity.id,
        name = playlistEntity.name,
        description = playlistEntity.description,
        coverUri = playlistEntity.coverUri,
        tracksIds = playlistEntity.tracksIds,
        tracksCount = playlistEntity.tracksCount
    )
}