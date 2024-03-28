package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.data.IMediaPlayerControlListener
import com.example.playlistmaker.media.data.Player
import com.example.playlistmaker.media.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val player: Player,
    private val gson: Gson,
    private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel() {
    private val _track = MutableLiveData<Track>()
    val track: LiveData<Track> = _track

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> = _currentTime

    fun setTrack(newTrackJson: String) {
        val newTrack = gson.fromJson(newTrackJson, Track::class.java)
        viewModelScope.launch {
            newTrack.isFavorite =
                favoriteTracksInteractor.isTrackFavorite(newTrack.trackId.toString())
            _track.postValue(newTrack)
        }
        if (!newTrack.previewUrl.isNullOrBlank()) {
            player.prepare(newTrack.previewUrl) {
                _isPlaying.value = false
                _currentTime.value = "00:00"
            }
        }
    }

    fun togglePlayback() {
        player.playbackControl(object : IMediaPlayerControlListener {
            override fun onStartPlayer() {
                _isPlaying.value = true
            }

            override fun onPausePlayer() {
                _isPlaying.value = false
            }

            override fun onTimeUpdate(time: String) {
                _currentTime.value = time
            }
        })
    }

    fun releasePlayer() {
        player.release()
    }

    fun pausePlayer() {
        player.pause {
            _isPlaying.value = false
            player.stopTimer()
        }
    }

    fun toggleAddToFavorites() {
        _track.value?.let { track ->
            viewModelScope.launch {
                if (track.isFavorite) {
                    favoriteTracksInteractor.deleteFromFavorites(track)
                    track.isFavorite = false
                    _track.postValue(track)
                } else {
                    track.addedTime = System.currentTimeMillis()
                    favoriteTracksInteractor.addToFavorites(track)
                    track.isFavorite = true
                    _track.postValue(track)
                }
            }
        }
    }
}