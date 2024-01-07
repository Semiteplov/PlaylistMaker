package com.example.playlistmaker.media.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.media.ui.view_model.MediaViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.loadTrackImage

class MediaActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMediaBinding.inflate(layoutInflater) }
    private val viewModel by lazy { ViewModelProvider(this)[MediaViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewModel()
        setListeners()
    }

    private fun setupViewModel() {
        viewModel.track.observe(this) { track ->
            setUIData(track)
        }

        viewModel.isPlaying.observe(this) { isPlaying ->
            if (isPlaying) {
                binding.ibPlay.setImageResource(R.drawable.ic_pause_button)
            } else {
                binding.ibPlay.setImageResource(R.drawable.ic_play_button)
            }
        }

        viewModel.currentTime.observe(this) { time ->
            binding.tvTimeTrack.text = time
        }

        processTrackData()
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
            viewModel.togglePlayback()
        }
    }

    private fun processTrackData() {
        intent.getStringExtra("key")?.let { json ->
            viewModel.setTrack(json)
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
            tvYearValue.text = track.releaseDate
            tvDurationValue.text = track.trackTime.replaceFirst("0", "")
            tvGenreValue.text = track.primaryGenreName
            loadTrackImage(this@MediaActivity, ivMain, track, true)
            tvTimeTrack.text = track.trackTime.replaceFirst("0", "")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }
}