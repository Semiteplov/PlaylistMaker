package com.example.playlistmaker.media.data

interface IMediaPlayerControlListener {
    fun onStartPlayer()
    fun onPausePlayer()
    fun onTimeUpdate(time: String)
}