package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.media.ui.events.FavoriteTracksScreenEvent
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor) :
    ViewModel() {
    private val _tracks = MutableLiveData<List<Track>>(listOf())
    val tracks: LiveData<List<Track>> = _tracks

    val event = SingleLiveEvent<FavoriteTracksScreenEvent>()

    init {
        subscribeOnFavoriteTracks()
    }

    fun onTrackClicked(track: Track) {
        favoriteTracksInteractor.saveTrackForPlaying(track)
        event.value = FavoriteTracksScreenEvent.OpenPlayerScreen
    }

    private fun subscribeOnFavoriteTracks() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksInteractor.getFavoriteTracks().collect { _tracks.postValue(it) }
        }
    }
}