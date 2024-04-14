package com.example.playlistmaker.media.ui.model

import com.example.playlistmaker.media.data.PlayerState
import com.example.playlistmaker.search.domain.models.Track

data class PlayerScreenState(
    val playerState: PlayerState = PlayerState.STATE_PREPARED,
    val track: Track? = null,
    val trackTime: String = "",
    var isFavoriteTrack: Boolean = false
)