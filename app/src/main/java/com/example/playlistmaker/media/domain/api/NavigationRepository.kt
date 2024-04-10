package com.example.playlistmaker.media.domain.api

import androidx.lifecycle.LiveData

interface NavigationRepository {
    val isBottomNavigationVisible: LiveData<Boolean>

    fun setBottomNavigationVisibility(isVisible: Boolean)
}