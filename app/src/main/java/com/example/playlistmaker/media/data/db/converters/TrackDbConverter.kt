package com.example.playlistmaker.media.data.db.converters

import com.example.playlistmaker.media.data.db.entities.TrackEntity
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConverter {
    fun map(track: Track) = TrackEntity(
        id = track.trackId,
        trackName = track.trackTitle,
        artistName = track.artistName,
        trackTimeMillis = track.trackTime,
        artworkUrl100 = track.artworkUrl100,
        primaryGenreName = track.primaryGenreName,
        collectionName = track.collectionName,
        country = track.country,
        releaseDate = track.releaseDate,
        previewUrl = track.previewUrl
    )
}