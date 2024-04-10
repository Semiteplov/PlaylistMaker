package com.example.playlistmaker.media.domain.impl

import com.example.playlistmaker.media.domain.PlayerInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track

class PlayerInteractorImpl(
    private val trackRepository: TracksRepository
) : PlayerInteractor {

    override fun getTrackForPlaying(): Track? = trackRepository.getTrackForPlaying()
}