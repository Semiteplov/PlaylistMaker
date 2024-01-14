package com.example.playlistmaker.media.di

import android.media.MediaPlayer
import com.example.playlistmaker.media.data.Player
import com.example.playlistmaker.media.ui.view_model.MediaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    single { MediaPlayer() }
    single { Player(get()) }
    viewModel { MediaViewModel(get()) }
}