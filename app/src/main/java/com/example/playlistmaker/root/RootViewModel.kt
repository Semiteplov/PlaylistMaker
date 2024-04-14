package com.example.playlistmaker.root

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.media.domain.db.NavigationInteractor

class RootViewModel(navigationInteractor: NavigationInteractor) : ViewModel() {
    val isBottomNavigationVisible = navigationInteractor.isBottomNavigationVisible
}