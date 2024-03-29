package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.data.Player
import com.example.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.media.ui.view_model.PlayerViewModel
import com.example.playlistmaker.media.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    viewModel { PlayerViewModel(Player, get()) }
    viewModel { FavoriteTracksViewModel() }
    viewModel { PlaylistsViewModel() }
}