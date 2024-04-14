package com.example.playlistmaker.media.data.db.converters

import com.example.playlistmaker.media.data.db.entities.FavoriteTrackEntity
import com.example.playlistmaker.search.domain.models.Track

class FavoriteTrackDbConvertor {
    fun map(track: FavoriteTrackEntity): Track {
        return Track(
            track.trackId,
            track.trackTitle,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.primaryGenreName,
            track.collectionName,
            track.country,
            track.releaseDate,
            track.previewUrl,
            addedTime = track.addedTime,
        )
    }

    fun map(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            track.trackId,
            track.trackTitle,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.primaryGenreName,
            track.collectionName,
            track.country,
            track.releaseDate,
            track.previewUrl,
            track.addedTime,
        )
    }
}