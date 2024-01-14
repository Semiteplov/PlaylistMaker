package com.example.playlistmaker.settings.data.utils

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class ThemeManager(private val themeRepository: ThemeRepository) {
    fun switchTheme(darkThemeEnabled: Boolean) {
        val newSettings = ThemeSettings(darkThemeEnabled)
        themeRepository.updateThemeSetting(newSettings)
        applyTheme(darkThemeEnabled)
    }

    fun applyTheme(darkThemeEnabled: Boolean) {
        val mode = if (darkThemeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}