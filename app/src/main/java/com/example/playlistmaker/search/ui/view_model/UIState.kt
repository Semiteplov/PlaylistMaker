package com.example.playlistmaker.search.ui.view_model

sealed class UIState {
    object Search : UIState()
    object History : UIState()
}