package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ImageView>(R.id.search).setOnClickListener {
            Toast.makeText(this@MainActivity, "Tapped on Search!", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.media).setOnClickListener {
            Toast.makeText(this@MainActivity, "Tapped on Media!", Toast.LENGTH_SHORT).show()
        }

        findViewById<ImageView>(R.id.settings).setOnClickListener {
            Toast.makeText(this@MainActivity, "Tapped on Settings!", Toast.LENGTH_SHORT).show()
        }
    }
}