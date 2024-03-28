package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.media.ui.FavoriteTracksAdapter
import com.example.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private var adapter: FavoriteTracksAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FavoriteTracksAdapter()
        setupRecyclerView()

        favoriteTracksViewModel.favoriteTracks.observe(viewLifecycleOwner) { tracks ->
            if (tracks.isEmpty()) {
                binding.apply {
                    ivNoTracks.visibility = View.VISIBLE
                    tvNoTracks.visibility = View.VISIBLE
                    rcFavoriteTracks.visibility = View.GONE
                }
            } else {
                binding.apply {
                    ivNoTracks.visibility = View.GONE
                    tvNoTracks.visibility = View.GONE
                    rcFavoriteTracks.visibility = View.VISIBLE
                }
                adapter?.updateTracks(tracks)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rcFavoriteTracks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteTracksFragment.adapter
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteTracksViewModel.getFavoriteTracks()
        favoriteTracksViewModel.favoriteTracks.value?.let { adapter?.updateTracks(it) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}