package com.example.playlistmaker.settings.data.impl

import android.content.Context
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class ThemeRepositoryImpl(private val context: Context) : ThemeRepository {

    companion object {
        private const val PREFERENCES_FILE_KEY = "com.example.playlistmaker.settings"
        private const val THEME_KEY = "theme_settings"
    }

    override fun getThemeSettings(): ThemeSettings {
        val sharedPreferences =
            context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        val isDarkThemeEnabled = sharedPreferences.getBoolean(THEME_KEY, false)
        return ThemeSettings(isDarkThemeEnabled)
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        val sharedPreferences =
            context.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(THEME_KEY, settings.isDarkThemeEnabled)
            apply()
        }
    }

}