package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.media.data.IMediaPlayerControlListener
import com.example.playlistmaker.media.data.Player
import com.google.gson.Gson

class MediaViewModel(private val player: Player) : ViewModel() {
    private val _track = MutableLiveData<Track>()
    val track: LiveData<Track> = _track

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> = _currentTime

    fun setTrack(newTrackJson: String) {
        val newTrack = Gson().fromJson(newTrackJson, Track::class.java)
        _track.value = newTrack
        if (!newTrack.previewUrl.isNullOrBlank()) {
            player.prepare(newTrack.previewUrl) {
                _isPlaying.value = false
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
}