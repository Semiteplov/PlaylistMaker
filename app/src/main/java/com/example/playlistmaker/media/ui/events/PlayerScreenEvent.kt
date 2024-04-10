package com.example.playlistmaker.media.ui.events

sealed class PlayerScreenEvent {
    object OpenPlaylistsBottomSheet : PlayerScreenEvent()
    object ClosePlaylistsBottomSheet : PlayerScreenEvent()
    data class ShowTrackAddedMessage(val playlistName: String) : PlayerScreenEvent()
    data class ShowTrackAlreadyInPlaylistMessage(val playlistName: String) : PlayerScreenEvent()
    object NavigateToCreatePlaylistScreen : PlayerScreenEvent()
}