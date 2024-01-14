package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import com.example.playlistmaker.media.di.mediaModule
import com.example.playlistmaker.settings.data.utils.ThemeManager
import com.example.playlistmaker.settings.di.settingsModule
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import com.example.playlistmaker.sharing.di.sharingModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    companion object {
        const val SAVED_HISTORY = "saved_history"
    }

    private val sharedSavedHistoryPrefs: SharedPreferences by lazy {
        getSharedPreferences(SAVED_HISTORY, MODE_PRIVATE)
    }
    private val themeRepository: ThemeRepository by inject()
    private val themeManager: ThemeManager by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(sharingModule, settingsModule, mediaModule)
        }
        setAppThemeOnAppStart()
        Creator.init(sharedSavedHistoryPrefs)
    }

    private fun setAppThemeOnAppStart() {
        val currentSettings = themeRepository.getThemeSettings()
        themeManager.applyTheme(currentSettings.isDarkThemeEnabled)
    }
}