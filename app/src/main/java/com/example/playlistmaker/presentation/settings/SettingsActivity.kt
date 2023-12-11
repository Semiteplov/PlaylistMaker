package com.example.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.main.MainActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<Toolbar>(R.id.back_button).setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        findViewById<Button>(R.id.share_button).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.practicum_catalog_link)
            )
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_app)))
        }

        findViewById<Button>(R.id.support_button).setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.slaviksis_yandex_ru)))
            emailIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.email_subject)
            )
            emailIntent.putExtra(
                Intent.EXTRA_TEXT,
                getString(R.string.email_text)
            )
            startActivity(emailIntent)
        }

        findViewById<Button>(R.id.terms_button).setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.yandex_offer_link)))
            startActivity(browserIntent)
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.theme_switcher)
        themeSwitcher.isChecked = (application as App).isDarkThemeEnabled
        themeSwitcher.setOnCheckedChangeListener { _, checked ->
            (application as App).switchTheme(checked)
        }
    }
}