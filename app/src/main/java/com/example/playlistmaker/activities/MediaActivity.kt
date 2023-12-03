package com.example.playlistmaker.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.services.MediaPlayerControlListener
import com.example.playlistmaker.services.Player
import com.example.playlistmaker.utils.loadTrackImage
import com.google.gson.Gson

class MediaActivity : AppCompatActivity(), MediaPlayerControlListener {
    private lateinit var binding: ActivityMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeUI()
        processTrackData()
        setListeners()
    }

    override fun onStartPlayer() {
        binding.ibPlay.setImageResource(R.drawable.ic_pause_button)
    }

    override fun onPausePlayer() {
        binding.ibPlay.setImageResource(R.drawable.ic_play_button)
    }

    override fun onTimeUpdate(time: String) {
        binding.tvTimeTrack.text = time
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
                this, getString(R.string.playlist_created), Toast.LENGTH_LONG
            ).show()
        }
        binding.ibPlay.setOnClickListener {
            Player.playbackControl(this)
        }
    }

    private fun processTrackData() {
        intent.getStringExtra("key")?.let { json ->
            Gson().fromJson(json, Track::class.java)?.let { track ->
                setUIData(track)
                if (!track.previewUrl.isNullOrBlank()) {
                    Player.prepare(track.previewUrl) {
                        binding.tvTimeTrack.text =
                            getString(R.string.default_track_time)
                        onPausePlayer()
                    }
                }
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

    override fun onDestroy() {
        super.onDestroy()
        Player.release()
    }

    override fun onPause() {
        super.onPause()
        Player.pause {
            binding.ibPlay.setImageResource(R.drawable.ic_play_button)
            Player.stopTimer()
        }
    }
}