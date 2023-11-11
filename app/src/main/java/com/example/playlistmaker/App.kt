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
    private val sharedPrefs: SharedPreferences by lazy {
        getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
    }

    override fun onCreate() {
        super.onCreate()
        setAppThemeOnAppStart()
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

    private fun setAppThemeOnAppStart() {
        isDarkThemeEnabled  = sharedPrefs.getBoolean(DARK_THEME_ENABLED, false)
        switchTheme(isDarkThemeEnabled )
    }
}