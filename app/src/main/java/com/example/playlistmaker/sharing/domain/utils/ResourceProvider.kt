package com.example.playlistmaker.sharing.domain.utils

interface ResourceProvider {
    fun getString(id: Int): String
}