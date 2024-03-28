package com.example.playlistmaker.media.data.db.converters

import com.example.playlistmaker.media.data.db.entities.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConvertor {
    fun map(track: TrackEntity): Track {
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

    fun map(track: Track): TrackEntity {
        return TrackEntity(
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