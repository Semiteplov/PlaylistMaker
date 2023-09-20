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
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private var savedText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupUI(savedInstanceState)
        setUpUpdateButton()
    }

    private fun setupUI(savedInstanceState: Bundle?) {
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
            binding.updateButton.visibility = View.INVISIBLE
            hideKeyboard()
            hidePicture()
            clearAdapter()
        }
        setUpSearchButton()
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
            getWebRequest(binding.search.text.toString().trim())
            false
        }
    }

    private fun setUpUpdateButton() {
        binding.updateButton.setOnClickListener {
            getWebRequest(
                binding.search.text.toString().trim()
            )
        }
    }

    private fun getWebRequest(query: String) {
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
        val trackList = response.body()?.results.orEmpty()
        if (response.isSuccessful && trackList.isNotEmpty()) {
            displayTracks(trackList)
        } else {
            displayError(response)
        }
    }

    private fun displayTracks(trackList: List<Track>) {
        binding.apply {
            trackRecyclerView.visibility = View.VISIBLE
            noTracksImage.visibility = View.INVISIBLE
            updateButton.visibility = View.INVISIBLE
            text.text = ""
            trackRecyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
            trackRecyclerView.adapter = TrackAdapter().apply {
                set(trackList)
            }
        }
    }

    private fun displayError(response: Response<ITunesResponse>) {
        binding.apply {
            trackRecyclerView.visibility = View.INVISIBLE
            noTracksImage.visibility = View.VISIBLE
            text.visibility = View.VISIBLE
            updateButton.visibility = View.INVISIBLE

            if (response.isSuccessful) {
                text.text = getString(R.string.no_tracks)
                noTracksImage.setImageResource(
                    if (isNightModeEnabled()) R.drawable.ic_no_tracks_dark else R.drawable.ic_no_tracks
                )
            } else {
                text.text = getString(R.string.network_error)
                noTracksImage.setImageResource(
                    if (isNightModeEnabled()) R.drawable.ic_network_error_dark else R.drawable.ic_network_error
                )
            }
        }
    }

    private fun handleFailure() {
        with(binding) {
            trackRecyclerView.visibility = View.INVISIBLE
            noTracksImage.visibility = View.VISIBLE
            text.visibility = View.VISIBLE
            updateButton.visibility = View.VISIBLE
            text.text = getString(R.string.network_error)
            noTracksImage.setImageResource(
                if (isNightModeEnabled()) R.drawable.ic_network_error_dark
                else R.drawable.ic_network_error
            )
        }
    }

    private fun isNightModeEnabled(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }
}
