package com.example.playlistmaker.settings.domain.api

import com.example.playlistmaker.settings.domain.model.ThemeSettings

interface ThemeRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}
