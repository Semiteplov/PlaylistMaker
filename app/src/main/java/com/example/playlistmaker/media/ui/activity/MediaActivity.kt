package com.example.playlistmaker.media.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.media.ui.MediaViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMediaBinding.inflate(layoutInflater) }
    private val tabMediator by lazy {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator.attach()

        binding.toolbarMedia.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}