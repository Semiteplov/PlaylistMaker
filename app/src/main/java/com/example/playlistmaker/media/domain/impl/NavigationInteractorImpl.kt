package com.example.playlistmaker.media.domain.impl

import androidx.lifecycle.LiveData
import com.example.playlistmaker.media.domain.api.NavigationRepository
import com.example.playlistmaker.media.domain.db.NavigationInteractor

class NavigationInteractorImpl(
    private val navigationRepository: NavigationRepository
) : NavigationInteractor {

    override val isBottomNavigationVisible: LiveData<Boolean> =
        navigationRepository.isBottomNavigationVisible

    override fun setBottomNavigationVisibility(isVisible: Boolean) {
        navigationRepository.setBottomNavigationVisibility(isVisible)
    }
}