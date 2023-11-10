package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    companion object {
        const val SHARED_PREFERENCES = "shared_preferences"
        const val DARK_THEME_ENABLED = "dark_theme_enabled"
    }

    var isDarkThemeEnabled = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        switchTheme(sharedPrefs.getBoolean(DARK_THEME_ENABLED, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        isDarkThemeEnabled = darkThemeEnabled
        sharedPrefs.edit().putBoolean(DARK_THEME_ENABLED, isDarkThemeEnabled).apply()

        val mode = if (darkThemeEnabled) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

}