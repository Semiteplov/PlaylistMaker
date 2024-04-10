package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.Debouncer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor,
    private val searchInteractor: SearchInteractor
) : ViewModel() {
    private val _tracks = MutableLiveData<List<Track>>()
    val tracks: LiveData<List<Track>> = _tracks

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _searchHistory = MutableLiveData<List<Track>>()
    val searchHistory: LiveData<List<Track>> = _searchHistory

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState> = _uiState

    fun searchTracks(query: String) {
        if (query.isNotEmpty()) {
            _uiState.value = UIState.Search
            viewModelScope.launch {
                Debouncer.requestDebounce {
                    tracksInteractor.searchTracks(query)
                        .onStart {
                            _isLoading.postValue(true)
                        }
                        .catch { _ ->
                            _isLoading.postValue(false)
                            _uiState.postValue(UIState.Error)
                        }
                        .collect { foundTracks ->
                            _isLoading.postValue(false)
                            if (foundTracks.isNotEmpty()) {
                                _tracks.postValue(foundTracks)
                                _uiState.postValue(UIState.Search)
                            } else {
                                _uiState.postValue(UIState.EmptyResult)
                            }
                        }
                }
            }
        } else {
            _isLoading.value = false
            _uiState.value = UIState.History
        }
    }

    fun loadSearchHistory() {
        _uiState.value = UIState.History
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

    fun saveTrackForPlaying(track: Track) {
        searchInteractor.saveTrackForPlaying(track)
    }
}