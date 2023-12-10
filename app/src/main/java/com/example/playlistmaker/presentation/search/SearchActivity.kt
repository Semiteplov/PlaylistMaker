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
            binding.historyViewSearch.visibility = View.GONE
            binding.clearHistoryButton.visibility = View.GONE
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
            Toast.makeText(this, "Трек сохранен в истории", Toast.LENGTH_SHORT).show()
        }
        historySearchAdapter = TrackAdapter({ track ->
            saveTrackToPreferences(track)
        }) { track ->
            searchHistoryInteractor.saveSearchHistory(track)
            Toast.makeText(this, "Трек сохранен в истории", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadSearchHistory() {
        val history = searchHistoryInteractor.getSearchHistory()
        historySearchAdapter.tracks = ArrayList(history)
        historySearchAdapter.notifyDataSetChanged()
        updateHistoryVisibility(history.isNotEmpty())
    }

    private fun updateHistoryVisibility(hasHistory: Boolean) {
        binding.historyViewSearch.visibility = if (hasHistory) View.VISIBLE else View.GONE
        binding.clearHistoryButton.visibility = if (hasHistory) View.VISIBLE else View.GONE
    }

    private fun setupUI(savedInstanceState: Bundle?) {
        binding.searchBackButton.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
        savedInstanceState?.let {
            savedText = it.getString(getString(R.string.search_text), "")
            binding.search.setText(savedText)
        }
        binding.search.doOnTextChanged { text, _, _, _ ->
            binding.historyRecyclerView.visibility =
                if (binding.search.hasFocus() && text?.isEmpty() == true) View.VISIBLE else View.GONE
            binding.historyViewSearch.visibility =
                if (binding.search.hasFocus() && text?.isEmpty() == true) View.VISIBLE else View.GONE
            binding.clearHistoryButton.visibility =
                if (binding.search.hasFocus() && text?.isEmpty() == true) View.VISIBLE else View.GONE

            historySearchAdapter.notifyDataSetChanged()

            binding.clearButton.visibility = clearButtonVisibility(text)
            binding.rvProgressBar.isVisible = true

            Debouncer.requestDebounce {
                getTracks(binding.search.text.toString().trim())
            }
        }
        binding.clearButton.setOnClickListener {
            binding.search.text?.clear()
            binding.updateButton.visibility = View.INVISIBLE
            hideKeyboard()
            hidePicture()
            clearAdapter()

            historySearchAdapter.clear()
            val history = searchHistoryInteractor.getSearchHistory()
            historySearchAdapter.set(history)
            historySearchAdapter.notifyDataSetChanged()
            if (history.isEmpty()) {
                binding.historyViewSearch.visibility = View.GONE
                binding.clearHistoryButton.visibility = View.GONE
            } else {
                binding.historyViewSearch.visibility = View.VISIBLE
                binding.clearHistoryButton.visibility = View.VISIBLE
            }
            binding.historyRecyclerView.visibility = View.VISIBLE
            binding.noTracksImage.visibility = View.GONE
            binding.text.visibility = View.GONE
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
            if (query.isNotEmpty()) {
                getTracks(query)
            }
        }
    }

    private fun getTracks(query: String) {
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
