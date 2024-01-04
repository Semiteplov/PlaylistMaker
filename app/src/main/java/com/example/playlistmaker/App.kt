package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class App : Application() {
    private lateinit var themeRepository: ThemeRepositoryImpl

    override fun onCreate() {
        super.onCreate()
        themeRepository = ThemeRepositoryImpl(this)
        setAppThemeOnAppStart()
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