package com.example.playlistmaker.search.di

import android.content.Context
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.network.ITunesApi
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.search.view_model.SearchViewModel
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executor
import java.util.concurrent.Executors

val searchModule = module {
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single<NetworkClient> { RetrofitNetworkClient(get()) }

    single {
        androidContext().getSharedPreferences("saved_history", Context.MODE_PRIVATE)
    }

    factory { GsonBuilder().create() }

    single<SearchHistoryRepository> { SearchHistoryRepositoryImpl(get(), get()) }
    single<SearchHistoryInteractor> { SearchHistoryInteractorImpl(get()) }

    single<Executor> { Executors.newSingleThreadExecutor() }

    single<TracksRepository> { TracksRepositoryImpl(get()) }
    single<TracksInteractor> { TracksInteractorImpl(get(), get()) }

    viewModel { SearchViewModel(get(), get()) }
}