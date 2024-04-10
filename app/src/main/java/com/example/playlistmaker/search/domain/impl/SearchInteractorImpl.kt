package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track

class SearchInteractorImpl(private val trackRepository: TracksRepository) :
    SearchInteractor {
    override fun saveTrackForPlaying(track: Track) = trackRepository.saveTrackForPlaying(track)
}