package com.example.playlistmaker.settings.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.main.ui.activity.MainActivity
import com.example.playlistmaker.settings.data.impl.ThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import com.example.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.impl.ResourceProviderImpl
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl

class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupViewModel()
        observeViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        val app = application as App
        val settingsInteractor = SettingsInteractorImpl(ThemeRepositoryImpl(this))
        val sharingInteractor = SharingInteractorImpl(ExternalNavigatorImpl(this))
        val resourceProvider = ResourceProviderImpl(this)

        viewModel = ViewModelProvider(
            this, SettingsViewModel.getViewModelFactory(
                app, sharingInteractor, settingsInteractor, resourceProvider
            )
        )[SettingsViewModel::class.java]
    }

    private fun observeViewModel() {
        viewModel.navigateToMain.observe(this) { navigate ->
            if (navigate) {
                startActivity(Intent(this, MainActivity::class.java))
                viewModel.navigatedToMain()
            }
        }

        viewModel.themeSettings.observe(this) { settings ->
            binding.themeSwitcher.isChecked = settings.isDarkThemeEnabled
        }
    }

    private fun setupListeners() {
        binding.apply {
            backButton.setOnClickListener { viewModel.backClicked() }
            themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
                viewModel.switchTheme(isChecked)
            }
            shareButton.setOnClickListener { viewModel.shareApp() }
            supportButton.setOnClickListener { viewModel.sendEmail() }
            termsButton.setOnClickListener { viewModel.openTerms() }
        }
    }
}