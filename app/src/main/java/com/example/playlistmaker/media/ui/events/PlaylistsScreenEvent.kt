package com.example.playlistmaker.media.ui.events

sealed class PlaylistsScreenEvent {
    object NavigateToNewPlaylist : PlaylistsScreenEvent()
}