package com.example.playlistmaker.media.domain.db

import androidx.lifecycle.LiveData

interface NavigationInteractor {
    val isBottomNavigationVisible: LiveData<Boolean>

    fun setBottomNavigationVisibility(isVisible: Boolean)
}