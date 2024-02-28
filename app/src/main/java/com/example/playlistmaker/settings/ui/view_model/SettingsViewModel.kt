package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.settings.data.utils.ThemeManager
import com.example.playlistmaker.settings.domain.api.SettingsInteractor
import com.example.playlistmaker.settings.domain.model.ThemeSettings
import com.example.playlistmaker.sharing.data.model.EmailData
import com.example.playlistmaker.sharing.domain.api.SharingInteractor
import com.example.playlistmaker.sharing.domain.utils.ResourceProvider

class SettingsViewModel(
    private val themeManager: ThemeManager,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
    private val resourceProvider: ResourceProvider,
) : ViewModel() {
    private val _themeSettings = MutableLiveData<ThemeSettings>()
    val themeSettings: LiveData<ThemeSettings> = _themeSettings

    init {
        loadThemeSettings()
    }

    private fun loadThemeSettings() {
        _themeSettings.value = settingsInteractor.getThemeSettings()
    }

    fun shareApp() {
        sharingInteractor.shareApp(
            resourceProvider.getString(R.string.practicum_catalog_link),
            resourceProvider.getString(R.string.share_app)
        )
    }

    fun openTerms() {
        sharingInteractor.openTerms(resourceProvider.getString(R.string.yandex_offer_link))
    }

    fun sendEmail() {
        sharingInteractor.sendEmail(
            EmailData(
                resourceProvider.getString(R.string.slaviksis_yandex_ru),
                resourceProvider.getString(R.string.email_subject),
                resourceProvider.getString(R.string.email_text)
            )
        )
    }

    fun switchTheme(isDarkThemeEnabled: Boolean) {
        val newSettings = ThemeSettings(isDarkThemeEnabled)
        settingsInteractor.updateThemeSetting(newSettings)
        _themeSettings.value = newSettings

        themeManager.switchTheme(isDarkThemeEnabled)
    }
}