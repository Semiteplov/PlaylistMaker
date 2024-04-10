package com.example.playlistmaker.media.di

import androidx.room.Room
import com.example.playlistmaker.media.data.db.AppDatabase
import com.example.playlistmaker.media.data.db.converters.FavoriteTrackDbConvertor
import com.example.playlistmaker.media.data.db.converters.ListConverter
import com.example.playlistmaker.media.data.db.converters.PlaylistDbConverter
import com.example.playlistmaker.media.data.db.converters.TrackDbConverter
import com.example.playlistmaker.media.data.db.dao.FavoriteTrackDao
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.dao.TrackDao
import com.example.playlistmaker.media.data.impl.FavoriteTracksRepositoryImpl
import com.example.playlistmaker.media.data.impl.PlaylistRepositoryImpl
import com.example.playlistmaker.media.data.impl.navigation.NavigationRepositoryImpl
import com.example.playlistmaker.media.data.impl.storage.external.ExternalStorageRepositoryImpl
import com.example.playlistmaker.media.domain.PlayerInteractor
import com.example.playlistmaker.media.domain.api.ExternalStorageRepository
import com.example.playlistmaker.media.domain.api.FavoriteTracksRepository
import com.example.playlistmaker.media.domain.api.NavigationRepository
import com.example.playlistmaker.media.domain.api.PlaylistRepository
import com.example.playlistmaker.media.domain.db.FavoriteTracksInteractor
import com.example.playlistmaker.media.domain.db.NavigationInteractor
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.media.domain.impl.FavoriteTracksInteractorImpl
import com.example.playlistmaker.media.domain.impl.NavigationInteractorImpl
import com.example.playlistmaker.media.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.media.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import com.example.playlistmaker.media.ui.view_model.NewPlaylistViewModel
import com.example.playlistmaker.media.ui.view_model.PlayerViewModel
import com.example.playlistmaker.media.ui.view_model.PlaylistsViewModel
import com.example.playlistmaker.search.domain.api.SearchInteractor
import com.example.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaModule = module {
    viewModel { FavoriteTracksViewModel(get()) }
    viewModel {
        PlaylistsViewModel(
            navigationInteractor = get(),
            playlistInteractor = get()
        )
    }
    viewModel<PlayerViewModel> {
        PlayerViewModel(
            favoriteTracksInteractor = get(),
            playlistInteractor = get(),
            playerInteractor = get(),
            application = androidApplication()
        )
    }
    viewModel<NewPlaylistViewModel> {
        NewPlaylistViewModel(navigationInteractor = get(), playlistInteractor = get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .addTypeConverter(ListConverter(get<Gson>()))
            .build()
    }
    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(get(), get())
    }
    single<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl(get(), get())
    }
    single<NavigationRepository> {
        NavigationRepositoryImpl()
    }
    single<NavigationInteractor> {
        NavigationInteractorImpl(get())
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get())
    }
    single<PlaylistInteractor> {
        PlaylistInteractorImpl(get(), get())
    }
    single<ExternalStorageRepository> {
        ExternalStorageRepositoryImpl(context = androidContext())
    }
    single<PlayerInteractor> {
        PlayerInteractorImpl(trackRepository = get())
    }
    single<SearchInteractor> {
        SearchInteractorImpl(get())
    }

    factory { FavoriteTrackDbConvertor() }
    factory { TrackDbConverter() }
    factory { PlaylistDbConverter() }
    factory { ListConverter(get()) }

    single<PlaylistDao> { get<AppDatabase>().playlistDao() }
    single<TrackDao> { get<AppDatabase>().trackDao() }
    single<FavoriteTrackDao> { get<AppDatabase>().favoriteTrackDao() }

    factory<Gson> { Gson() }
}