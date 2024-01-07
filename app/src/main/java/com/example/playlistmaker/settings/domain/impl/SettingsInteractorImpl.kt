package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.api.ThemeRepository
import com.example.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(private val themeRepository: ThemeRepository) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return themeRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        themeRepository.updateThemeSetting(settings)
    }

}