package com.example.playlistmaker

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private var savedText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBackButton.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        savedInstanceState?.let {
            savedText = it.getString(getString(R.string.search_text), "")
            binding.search.setText(savedText)
        }

        binding.search.doOnTextChanged { text, _, _, _ ->
            binding.clearButton.visibility =
                if (text.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
        }

        binding.clearButton.setOnClickListener {
            binding.search.text?.clear()
            hideKeyboard()
            hidePicture()
            clearAdapter()
        }

        setUpSearchButton()
        updateData()
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun hidePicture() {
        binding.noTracksImage.visibility = View.INVISIBLE
        binding.text.visibility = View.INVISIBLE
    }

    private fun clearAdapter() {
        val adapter = TrackAdapter()
        adapter.clear()
        binding.trackRecyclerView.adapter = adapter
    }

    private fun setUpSearchButton() {
        binding.search.setOnEditorActionListener { _, _, _ ->
            clearAdapter()
            getWebRequest()
            false
        }
    }

    private fun updateData() {
        binding.updateButton.setOnClickListener { getWebRequest() }
    }

    private fun getWebRequest() {
        val query = binding.search.text.toString().trim()
        val apiService = ITunesApiService.instance.iTunesApi

        apiService.search(query).enqueue(object : Callback<ITunesResponse> {
            override fun onResponse(
                call: Call<ITunesResponse>,
                response: Response<ITunesResponse>
            ) {
                handleResponse(response)
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                handleFailure()
            }
        })
    }

    private fun handleResponse(response: Response<ITunesResponse>) {
        val trackList = response.body()?.results ?: emptyList()

        if (response.isSuccessful && trackList.isNotEmpty()) {
            binding.trackRecyclerView.visibility = View.VISIBLE
            binding.noTracksImage.visibility = View.INVISIBLE
            binding.updateButton.visibility = View.INVISIBLE
            binding.text.text = ""

            val trackAdapter = TrackAdapter()
            binding.trackRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            binding.trackRecyclerView.adapter = trackAdapter
            trackAdapter.set(trackList)

        } else {
            binding.trackRecyclerView.visibility = View.INVISIBLE
            binding.noTracksImage.visibility = View.VISIBLE
            binding.text.visibility = View.VISIBLE
            binding.updateButton.visibility = View.INVISIBLE

            if (response.isSuccessful) {
                binding.text.text = getString(R.string.no_tracks)
                binding.noTracksImage.setImageResource(
                    if (isNightModeEnabled()) R.drawable.ic_no_tracks_dark
                    else R.drawable.ic_no_tracks
                )
            } else {
                binding.text.text = getString(R.string.network_error)
                binding.noTracksImage.setImageResource(
                    if (isNightModeEnabled()) R.drawable.ic_network_error_dark
                    else R.drawable.ic_network_error
                )
            }
        }
    }

    private fun handleFailure() {
        binding.trackRecyclerView.visibility = View.INVISIBLE
        binding.noTracksImage.visibility = View.VISIBLE
        binding.text.visibility = View.VISIBLE
        binding.updateButton.visibility = View.VISIBLE
        binding.text.text = getString(R.string.network_error)
        binding.noTracksImage.setImageResource(
            if (isNightModeEnabled()) R.drawable.ic_network_error_dark
            else R.drawable.ic_network_error
        )
    }

    private fun isNightModeEnabled(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}
