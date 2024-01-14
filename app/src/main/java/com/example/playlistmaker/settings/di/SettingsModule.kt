package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.utils.ThemeManager
import com.example.playlistmaker.settings.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<ThemeRepository> { ThemeRepositoryImpl(androidContext()) }
    single { ThemeManager(get()) }
    single<SettingsInteractor> { SettingsInteractorImpl(get()) }

    viewModel { SettingsViewModel(get(), get(), get(), get()) }
}
