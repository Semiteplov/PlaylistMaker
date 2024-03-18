package com.example.playlistmaker.search.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.utils.Debouncer
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var savedText: String = ""

    private var adapter: TrackAdapter? = null
    private var historySearchAdapter: TrackAdapter? = null
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        viewModel.tracks.observe(viewLifecycleOwner) { tracks ->
            displayTracks(tracks)
        }

        viewModel.isError.observe(viewLifecycleOwner) { isError ->
            if (isError) displayError() else displayTracks(viewModel.tracks.value ?: emptyList())
        }

        viewModel.isNetworkError.observe(viewLifecycleOwner) { isNetworkError ->
            if (isNetworkError) {
                handleNetworkError()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            with(binding) {
                rvProgressBar.isVisible = isLoading
                if (isLoading) hidePicture()
            }

        }

        viewModel.searchHistory.observe(viewLifecycleOwner) { history ->
            historySearchAdapter?.updateTracks(history)
            updateHistoryVisibility(history.isNotEmpty())
        }
    }

    private fun setupRecyclerViews() {
        binding.trackRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.adapter
        }

        binding.historyRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@SearchFragment.historySearchAdapter
        }
    }

    private fun setupAdapters() {
        adapter = TrackAdapter({ track ->
            viewModel.saveTrackToHistory(track)
        }) { track ->
            viewModel.saveSearchHistory(track)
            Toast.makeText(
                requireContext(),
                getString(R.string.track_saved_to_history),
                Toast.LENGTH_SHORT
            )
                .show()
        }
        historySearchAdapter = TrackAdapter({ track ->
            viewModel.saveTrackToHistory(track)
        }) { track ->
            viewModel.saveSearchHistory(track)
            Toast.makeText(
                requireContext(),
                getString(R.string.track_saved_to_history),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun updateHistoryVisibility(hasHistory: Boolean) {
        binding.historyViewSearch.isVisible = hasHistory
        binding.clearHistoryButton.isVisible = hasHistory
    }

    private fun setupUI(savedInstanceState: Bundle?) {
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
            binding.search.hasFocus() && text.isNullOrEmpty() && (adapter?.tracks?.isEmpty()
                ?: true)
        with(binding) {
            historyRecyclerView.isVisible = isHistoryVisible
            historyViewSearch.isVisible = isHistoryVisible
            clearHistoryButton.isVisible = isHistoryVisible
            clearButton.visibility = clearButtonVisibility(text)
        }

        if (!isHistoryVisible) {
            Debouncer.requestDebounce {
                viewModel.searchTracks(binding.search.text.toString().trim())
                binding.rvProgressBar.isVisible = true
            }
        }
    }

    private fun clearSearch() {
        binding.search.text?.clear()
        binding.updateButton.isVisible = false
        hideKeyboard()
        hidePicture()
        clearAdapter()

        adapter?.updateTracks(emptyList())
        binding.noTracksImage.isVisible = false
        binding.text.isVisible = false
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val inputManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun hidePicture() {
        binding.noTracksImage.visibility = View.INVISIBLE
        binding.text.visibility = View.INVISIBLE
    }

    private fun clearAdapter() {
        adapter?.updateTracks(emptyList())
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
            adapter?.updateTracks(trackList)
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
            text.text = getString(com.example.playlistmaker.R.string.network_error)
            noTracksImage.setImageResource(com.example.playlistmaker.R.drawable.ic_network_error)
        }
    }

    private fun clearButtonVisibility(text: CharSequence?): Int {
        binding.historyRecyclerView.visibility =
            if (text.isNullOrEmpty()) View.VISIBLE else View.GONE
        return if (text.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}