package com.example.playlistmaker.media.domain

import com.example.playlistmaker.search.domain.models.Track

interface PlayerInteractor {

    fun getTrackForPlaying(): Track?
}