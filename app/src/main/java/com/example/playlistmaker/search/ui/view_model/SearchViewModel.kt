package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.Debouncer

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {
    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isNetworkError = MutableLiveData<Boolean>()
    val isNetworkError: LiveData<Boolean> = _isNetworkError

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchHistory = MutableLiveData<List<Track>>()
    val searchHistory: LiveData<List<Track>> = _searchHistory

    fun searchTracks(query: String) {
        if (query.isNotEmpty()) {
            _isLoading.value = true
            Debouncer.requestDebounce {
                tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: List<Track>) {
                        _isLoading.postValue(false)
                        if (foundTracks.isNotEmpty()) {
                            _tracks.postValue(foundTracks)
                            _isError.postValue(false)
                            _isNetworkError.postValue(false)
                        } else {
                            _isError.postValue(true)
                        }
                    }

                    override fun onError(error: Throwable) {
                        _isLoading.postValue(false)
                        _isNetworkError.postValue(true)
                    }
                })
            }
        } else {
            _isLoading.value = false
        }
    }

    fun loadSearchHistory() {
        _searchHistory.value = searchHistoryInteractor.getSearchHistory()
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearSearchHistory()
        _searchHistory.value = emptyList()
    }

    fun saveTrackToHistory(track: Track) {
        searchHistoryInteractor.saveTrackToHistory(track)
    }

    fun saveSearchHistory(track: Track) {
        searchHistoryInteractor.saveSearchHistory(track)
    }
}