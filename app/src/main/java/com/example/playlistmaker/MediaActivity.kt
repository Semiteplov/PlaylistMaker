package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.google.gson.Gson

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeUI()
        setListeners()
        processIntentData()
    }

    private fun initializeUI() {
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setListeners() {
        binding.toolbarMedia.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.ibLike.setOnClickListener {
            binding.ibLike.setImageResource(R.drawable.ic_like)
            Toast.makeText(
                this,
                getString(R.string.playlist_created),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun processIntentData() {
        intent.getStringExtra("key")?.let { json ->
            Gson().fromJson(json, Track::class.java)?.let { track ->
                setUIData(track)
            }
        }
    }

    private fun setUIData(track: Track) {
        with(binding) {
            tvArtistName.text = track.artistName
            tvTrackName.text = track.trackTitle
            if (track.collectionName.isNotEmpty()) {
                tvAlbumValue.text = track.collectionName
            } else {
                tvAlbumValue.visibility = View.GONE
                tvAlbum.visibility = View.GONE
            }

            tvCountryValue.text = track.country
            tvYearValue.text = track.getYearFromReleaseDate()
            tvDurationValue.text = track.getFormattedTime().replaceFirst("0", "")
            tvGenreValue.text = track.primaryGenreName
            ivMain.loadTrackImage(this@MediaActivity, ivMain, track, true)
            tvTimeTrack.text = track.getFormattedTime().replaceFirst("0", "")
        }
    }
}
