package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class App : Application() {

    companion object {
        const val SAVED_HISTORY = "saved_history"
    }

    private val sharedSavedHistoryPrefs: SharedPreferences by lazy {
        getSharedPreferences(SAVED_HISTORY, MODE_PRIVATE)
    }
    private lateinit var themeRepository: ThemeRepositoryImpl

    override fun onCreate() {
        super.onCreate()
        themeRepository = ThemeRepositoryImpl(this)
        setAppThemeOnAppStart()
        Creator.init(sharedSavedHistoryPrefs)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        val newSettings = ThemeSettings(darkThemeEnabled)
        themeRepository.updateThemeSetting(newSettings)
        applyTheme(darkThemeEnabled)
    }

    private fun setAppThemeOnAppStart() {
        val currentSettings = themeRepository.getThemeSettings()
        applyTheme(currentSettings.isDarkThemeEnabled)
    }

    private fun applyTheme(darkThemeEnabled: Boolean) {
        val mode = if (darkThemeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}