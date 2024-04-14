package com.example.playlistmaker.media.domain.api

import android.net.Uri

interface ExternalStorageRepository {
    suspend fun savePlaylistCover(playlistId: String, uri: Uri): String
}