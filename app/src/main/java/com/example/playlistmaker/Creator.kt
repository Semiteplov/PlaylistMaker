package com.example.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private lateinit var sharedPreferences: SharedPreferences

    fun init(sharedPreferences: SharedPreferences) {
        this.sharedPreferences = sharedPreferences
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    private fun getSearchHistoryRepository(): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(sharedPreferences)
    }

    fun provideTracksInteractor(): TracksInteractor = TracksInteractorImpl(getTracksRepository())

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor = SearchHistoryInteractorImpl(
        getSearchHistoryRepository()
    )
}