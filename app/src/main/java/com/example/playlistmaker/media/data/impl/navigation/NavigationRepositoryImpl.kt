package com.example.playlistmaker.media.data.impl.navigation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.media.domain.api.NavigationRepository


class NavigationRepositoryImpl : NavigationRepository {

    private val _isBottomNavigationVisible = MutableLiveData(true)
    override val isBottomNavigationVisible: LiveData<Boolean> = _isBottomNavigationVisible

    override fun setBottomNavigationVisibility(isVisible: Boolean) {
        _isBottomNavigationVisible.value = isVisible
    }
}