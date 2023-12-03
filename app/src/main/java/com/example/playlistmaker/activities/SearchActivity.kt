package com.example.playlistmaker.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.network.ITunesApiService
import com.example.playlistmaker.network.ITunesResponse
import com.example.playlistmaker.R
import com.example.playlistmaker.models.SearchHistory
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.adapters.TrackAdapter
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.utils.Debouncer
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private var savedText: String = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchHistory: SearchHistory

    private lateinit var adapter: TrackAdapter
    private lateinit var historySearchAdapter: TrackAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(SAVED_HISTORY, Context.MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        adapter = TrackAdapter({ track -> saveTrackToPreferences(track) }) { track ->
            searchHistory.writeSearchHistory(track)
            Toast.makeText(this, "Трек сохранен в истории", Toast.LENGTH_SHORT).show()
        }
        historySearchAdapter = TrackAdapter({ track -> saveTrackToPreferences(track) }) { track ->
            searchHistory.writeSearchHistory(track)
            Toast.makeText(this, "Трек сохранен в истории", Toast.LENGTH_SHORT).show()
        }

        setupUI(savedInstanceState)
        setUpUpdateButton()

        binding.historyRecyclerView.layoutManager =
            LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historySearchAdapter

        binding.trackRecyclerView.layoutManager =
            LinearLayoutManager(this)
        binding.trackRecyclerView.adapter = adapter

        val trackListHistory = sharedPreferences.getString(SearchHistory.KEY_SEARCH_HISTORY, null)
        if (trackListHistory != null) {
            historySearchAdapter.tracks = ArrayList(searchHistory.readSearchHistory())
            historySearchAdapter.notifyDataSetChanged()
        }

        val hasSearchHistory = searchHistory.readSearchHistory().isNotEmpty()
        if (hasSearchHistory) {
            binding.historyViewSearch.visibility = View.VISIBLE
            binding.clearHistoryButton.visibility = View.VISIBLE
        } else {
            binding.historyViewSearch.visibility = View.GONE
            binding.clearHistoryButton.visibility = View.GONE
        }

        binding.clearHistoryButton.setOnClickListener {
            searchHistory.clearSearchHistory()
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
                getWebRequest(
                    binding.search.text.toString().trim()
                )
            }
        }
        binding.clearButton.setOnClickListener {
            binding.search.text?.clear()
            binding.updateButton.visibility = View.INVISIBLE
            hideKeyboard()
            hidePicture()
            clearAdapter()

            historySearchAdapter.clear()
            historySearchAdapter.set(searchHistory.readSearchHistory())
            historySearchAdapter.notifyDataSetChanged()
            if (searchHistory.readSearchHistory().isEmpty()) {
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
                binding.rvProgressBar.isVisible = false
            }

            override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                handleFailure()
                binding.rvProgressBar.isVisible = false
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
            adapter.updateTracks(trackList)
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
                noTracksImage.setImageResource(R.drawable.ic_no_tracks)
            } else {
                text.text = getString(R.string.network_error)
                noTracksImage.setImageResource(R.drawable.ic_network_error)
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
            noTracksImage.setImageResource(R.drawable.ic_network_error)
        }
    }

    private fun clearButtonVisibility(text: CharSequence?): Int {
        binding.historyRecyclerView.visibility =
            if (text.isNullOrEmpty()) View.VISIBLE else View.GONE
        return if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun saveTrackToPreferences(track: Track) {
        val trackJson = Gson().toJson(track)
        sharedPreferences.edit()
            .putString(TrackAdapter.NEW_TRACK_KEY, trackJson)
            .apply()
    }

    companion object {
        const val SAVED_HISTORY = "saved_history"
    }
}
