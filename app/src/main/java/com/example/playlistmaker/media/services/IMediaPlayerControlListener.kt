package com.example.playlistmaker.media.services

interface IMediaPlayerControlListener {
    fun onStartPlayer()
    fun onPausePlayer()
    fun onTimeUpdate(time: String)
}