package com.example.playlistmaker.media.di

import androidx.room.Room
import com.example.playlistmaker.media.data.Player
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.converters.TrackDbConvertor
import com.example.playlistmaker.media.data.impl.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.media.domain.api.FavoriteTracksRepository
import com.example.playlistmaker.media.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.media.domain.impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.media.ui.view_model.PlayerViewModel
import com.example.playlistmaker.media.ui.view_model.PlaylistsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    viewModel { PlayerViewModel(Player, get(), get()) }
    viewModel { FavoriteTracksViewModel() }
    viewModel { PlaylistsViewModel() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }
    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get())
    }

    factory { TrackDbConvertor() }
}