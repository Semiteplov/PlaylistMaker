package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryInteractorImpl(private val repository: SearchHistoryRepository) :
    SearchHistoryInteractor {
    override fun saveSearchHistory(track: Track) {
        repository.saveSearchHistory(track)
    }

    override fun getSearchHistory(): List<Track> {
        return repository.getSearchHistory()
    }

    override fun clearSearchHistory() {
        repository.clearSearchHistory()
    }

    override fun saveTrackToHistory(track: Track) {
        repository.saveTrackToHistory(track)
    }
}