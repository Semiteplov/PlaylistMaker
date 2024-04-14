package com.example.playlistmaker.media.ui.events

sealed class NewPlaylistEvent {
    object NavigateBack : NewPlaylistEvent()
    object ShowBackConfirmationDialog : NewPlaylistEvent()
    data class SetPlaylistCreatedResult(val playlistName: String) : NewPlaylistEvent()
}