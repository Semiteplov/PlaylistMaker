package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface SearchInteractor {
    fun saveTrackForPlaying(track: Track)
}