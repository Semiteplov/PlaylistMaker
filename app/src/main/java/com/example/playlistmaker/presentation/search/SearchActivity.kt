package com.example.playlistmaker.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.SearchHistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.Debouncer

class SearchActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private var savedText: String = ""

    private lateinit var adapter: TrackAdapter
    private lateinit var historySearchAdapter: TrackAdapter

    private val tracksInteractor: TracksInteractor by lazy { Creator.provideTracksInteractor() }
    private val searchHistoryInteractor: SearchHistoryInteractor by lazy { Creator.provideSearchHistoryInteractor() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupAdapters()
        setupUI(savedInstanceState)
        setUpUpdateButton()

        binding.historyRecyclerView.layoutManager =
            LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historySearchAdapter

        binding.trackRecyclerView.layoutManager =
            LinearLayoutManager(this)
        binding.trackRecyclerView.adapter = adapter

        loadSearchHistory()

        binding.clearHistoryButton.setOnClickListener {
            searchHistoryInteractor.clearSearchHistory()
            historySearchAdapter.clear()
            binding.historyViewSearch.isVisible = false
            binding.clearHistoryButton.isVisible = false
            historySearchAdapter.notifyDataSetChanged()
        }

        binding.search.setOnFocusChangeListener { _, hasFocus ->
            binding.historyRecyclerView.visibility =
                if (hasFocus && binding.search.text.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.search.setOnFocusChangeListener { _, hasFocus ->
            binding.clearHistoryButton.visibility =
                if (hasFocus && binding.search.text.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.search.setOnFocusChangeListener { _, hasFocus ->
            binding.historyViewSearch.visibility =
                if (!hasFocus && binding.search.text.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupAdapters() {
        adapter = TrackAdapter({ track ->
            saveTrackToPreferences(track)
        }) { track ->
            searchHistoryInteractor.saveSearchHistory(track)
            Toast.makeText(this, getString(R.string.track_saved_to_history), Toast.LENGTH_SHORT)
                .show()
        }
        historySearchAdapter = TrackAdapter({ track ->
            saveTrackToPreferences(track)
        }) { track ->
            searchHistoryInteractor.saveSearchHistory(track)
            Toast.makeText(this, getString(R.string.track_saved_to_history), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun loadSearchHistory() {
        val history = searchHistoryInteractor.getSearchHistory()
        historySearchAdapter.tracks = ArrayList(history)
        historySearchAdapter.notifyDataSetChanged()
        updateHistoryVisibility(history.isNotEmpty())
    }

    private fun updateHistoryVisibility(hasHistory: Boolean) {
        binding.historyViewSearch.isVisible = hasHistory
        binding.clearHistoryButton.isVisible = hasHistory
    }

    private fun setupUI(savedInstanceState: Bundle?) {
        binding.searchBackButton.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        savedInstanceState?.let {
            savedText = it.getString(getString(R.string.search_text), "")
            binding.search.setText(savedText)
        }
        binding.search.doOnTextChanged { text, _, _, _ ->
            val showHistory = binding.search.hasFocus() && text?.isEmpty() == true
            binding.historyRecyclerView.isVisible = showHistory
            binding.historyViewSearch.isVisible = showHistory
            binding.clearHistoryButton.isVisible = showHistory

            historySearchAdapter.notifyDataSetChanged()

            binding.clearButton.visibility = clearButtonVisibility(text)
            binding.rvProgressBar.isVisible = true

            Debouncer.requestDebounce {
                getTracks(binding.search.text.toString().trim())
            }
        }
        binding.clearButton.setOnClickListener {
            binding.search.text?.clear()
            binding.updateButton.isVisible = false
            hideKeyboard()
            hidePicture()
            clearAdapter()

            historySearchAdapter.clear()
            val history = searchHistoryInteractor.getSearchHistory()
            historySearchAdapter.set(history)
            historySearchAdapter.notifyDataSetChanged()
            if (history.isEmpty()) {
                binding.historyViewSearch.isVisible = false
                binding.clearHistoryButton.isVisible = false
            } else {
                binding.historyViewSearch.isVisible = true
                binding.clearHistoryButton.isVisible = true
            }
            binding.historyRecyclerView.isVisible = true
            binding.noTracksImage.isVisible = false
            binding.text.isVisible = false
        }
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
        adapter.updateTracks(emptyList())
    }

    private fun setUpUpdateButton() {
        binding.updateButton.setOnClickListener {
            val query = binding.search.text.toString().trim()
            getTracks(query)
        }
    }

    private fun getTracks(query: String) {
        if (query.isNotEmpty()) {
            tracksInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>) {
                    runOnUiThread {
                        if (foundTracks.isNotEmpty()) {
                            displayTracks(foundTracks)
                        } else {
                            displayError()
                        }
                        binding.rvProgressBar.isVisible = false
                    }
                }

                override fun onError(error: Throwable) {
                    runOnUiThread {
                        handleFailure()
                        binding.rvProgressBar.isVisible = false
                    }
                }
            })
        }
    }

    private fun displayTracks(trackList: List<Track>) {
        binding.apply {
            trackRecyclerView.visibility = View.VISIBLE
            noTracksImage.visibility = View.INVISIBLE
            updateButton.visibility = View.INVISIBLE
            text.text = ""
            adapter.updateTracks(trackList)
        }
    }

    private fun displayError() {
        binding.apply {
            trackRecyclerView.visibility = View.INVISIBLE
            noTracksImage.visibility = View.VISIBLE
            text.visibility = View.VISIBLE
            updateButton.visibility = View.INVISIBLE

            text.text = getString(R.string.no_tracks)
            noTracksImage.setImageResource(R.drawable.ic_no_tracks)

        }
    }

    private fun handleFailure() {
        with(binding) {
            trackRecyclerView.visibility = View.INVISIBLE
            noTracksImage.visibility = View.VISIBLE
            text.visibility = View.VISIBLE
            updateButton.visibility = View.VISIBLE
            text.text = getString(R.string.network_error)
            noTracksImage.setImageResource(R.drawable.ic_network_error)
        }
    }

    private fun clearButtonVisibility(text: CharSequence?): Int {
        binding.historyRecyclerView.visibility =
            if (text.isNullOrEmpty()) View.VISIBLE else View.GONE
        return if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun saveTrackToPreferences(track: Track) {
        searchHistoryInteractor.saveTrackToHistory(track)
    }
}
