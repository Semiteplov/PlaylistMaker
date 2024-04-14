package com.example.playlistmaker.media.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.NavigationInteractor
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.media.ui.events.PlaylistsScreenEvent
import com.example.playlistmaker.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val navigationInteractor: NavigationInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private val _playlists = MutableLiveData<List<Playlist>>(listOf())
    val playlists: LiveData<List<Playlist>> = _playlists

    val event = SingleLiveEvent<PlaylistsScreenEvent>()

    init {
        subscribeOnPlaylists()
    }

    fun onNewPlaylistButtonClicked() {
        navigationInteractor.setBottomNavigationVisibility(false)
        event.value = PlaylistsScreenEvent.NavigateToNewPlaylist
    }

    private fun subscribeOnPlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.getPlaylists().collect { _playlists.postValue(it) }
        }
    }
}