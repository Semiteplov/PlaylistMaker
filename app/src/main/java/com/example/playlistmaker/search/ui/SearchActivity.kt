package com.example.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.view_model.SearchViewModel
import com.example.playlistmaker.utils.Debouncer

class SearchActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private var savedText: String = ""

    private lateinit var adapter: TrackAdapter
    private lateinit var historySearchAdapter: TrackAdapter
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewModel()
        setupAdapters()

        setupRecyclerViews()
        setupUI(savedInstanceState)
        setUpUpdateButton()

        viewModel.loadSearchHistory()

        binding.clearHistoryButton.setOnClickListener {
            viewModel.clearSearchHistory()
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

    private fun setupViewModel() {
        val tracksInteractor = Creator.provideTracksInteractor()
        val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
        viewModel = ViewModelProvider(
            this, SearchViewModel.getViewModelFactory(
                tracksInteractor, searchHistoryInteractor
            )
        )[SearchViewModel::class.java]

        viewModel.tracks.observe(this) { tracks ->
            displayTracks(tracks)
        }

        viewModel.isError.observe(this) { isError ->
            if (isError) displayError() else displayTracks(viewModel.tracks.value ?: emptyList())
        }

        viewModel.isNetworkError.observe(this) { isNetworkError ->
            if (isNetworkError) {
                handleNetworkError()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            with(binding) {
                rvProgressBar.isVisible = isLoading
                if (isLoading) hidePicture()
            }

        }

        viewModel.searchHistory.observe(this) { history ->
            historySearchAdapter.updateTracks(history)
            updateHistoryVisibility(history.isNotEmpty())
        }
    }

    private fun setupRecyclerViews() {
        binding.trackRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.adapter
        }

        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = this@SearchActivity.historySearchAdapter
        }
    }

    private fun setupAdapters() {
        adapter = TrackAdapter({ track ->
            viewModel.saveTrackToHistory(track)
        }) { track ->
            viewModel.saveSearchHistory(track)
            Toast.makeText(this, getString(R.string.track_saved_to_history), Toast.LENGTH_SHORT)
                .show()
        }
        historySearchAdapter = TrackAdapter({ track ->
            viewModel.saveTrackToHistory(track)
        }) { track ->
            viewModel.saveSearchHistory(track)
            Toast.makeText(this, getString(R.string.track_saved_to_history), Toast.LENGTH_SHORT)
                .show()
        }
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
            updateSearchUI(text)
            hidePicture()
        }

        binding.clearButton.setOnClickListener {
            clearSearch()
        }

        binding.updateButton.setOnClickListener {
            val query = binding.search.text.toString().trim()
            viewModel.searchTracks(query)
        }
    }

    private fun updateSearchUI(text: CharSequence?) {
        val isHistoryVisible =
            binding.search.hasFocus() && text.isNullOrEmpty() && adapter.tracks.isEmpty()
        with(binding) {
            historyRecyclerView.isVisible = isHistoryVisible
            historyViewSearch.isVisible = isHistoryVisible
            clearHistoryButton.isVisible = isHistoryVisible
            clearButton.visibility = clearButtonVisibility(text)
        }

        if (!isHistoryVisible) {
            binding.rvProgressBar.isVisible = true
            Debouncer.requestDebounce {
                viewModel.searchTracks(binding.search.text.toString().trim())
            }
        }
    }

    private fun clearSearch() {
        binding.search.text?.clear()
        binding.updateButton.isVisible = false
        hideKeyboard()
        hidePicture()
        clearAdapter()

        adapter.updateTracks(emptyList())
        binding.noTracksImage.isVisible = false
        binding.text.isVisible = false
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
            viewModel.searchTracks(query)
        }
    }

    private fun displayTracks(trackList: List<Track>) {
        binding.apply {
            trackRecyclerView.isVisible = true
            noTracksImage.isVisible = false
            updateButton.isVisible = false
            text.text = ""
            adapter.updateTracks(trackList)
        }
    }

    private fun displayError() {
        binding.apply {
            trackRecyclerView.isVisible = false
            noTracksImage.isVisible = true
            text.isVisible = true
            updateButton.isVisible = false

            text.text = getString(R.string.no_tracks)
            noTracksImage.setImageResource(R.drawable.ic_no_tracks)
        }
    }

    private fun handleNetworkError() {
        with(binding) {
            trackRecyclerView.isVisible = false
            noTracksImage.isVisible = true
            text.isVisible = true
            updateButton.isVisible = true
            text.text = getString(R.string.network_error)
            noTracksImage.setImageResource(R.drawable.ic_network_error)
        }
    }

    private fun clearButtonVisibility(text: CharSequence?): Int {
        binding.historyRecyclerView.visibility =
            if (text.isNullOrEmpty()) View.VISIBLE else View.GONE
        return if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
    }
}
