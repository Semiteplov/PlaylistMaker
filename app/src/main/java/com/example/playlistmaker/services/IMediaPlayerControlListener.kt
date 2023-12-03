package com.example.playlistmaker.services

interface IMediaPlayerControlListener {
    fun onStartPlayer()
    fun onPausePlayer()
    fun onTimeUpdate(time: String)
}