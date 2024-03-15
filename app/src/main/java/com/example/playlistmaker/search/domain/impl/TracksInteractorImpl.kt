package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.models.Track
import java.util.concurrent.Executor

class TracksInteractorImpl(
    private val repository: TracksRepository,
    private val executor: Executor
) : TracksInteractor {

    override suspend fun searchTracks(expression: String): List<Track> {
        return repository.searchTracks(expression)
    }

}