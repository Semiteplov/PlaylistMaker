package com.example.playlistmaker.media.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.media.ui.activity.PlayerActivity
import com.example.playlistmaker.media.ui.adapters.FavoriteTracksAdapter
import com.example.playlistmaker.media.ui.events.FavoriteTracksScreenEvent
import com.example.playlistmaker.media.ui.view_model.FavoriteTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteTracksFragment : Fragment() {
    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel()
    private var binding: FragmentFavoritesBinding? = null
    private var adapter: FavoriteTracksAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = FavoriteTracksAdapter(favoriteTracksViewModel::onTrackClicked)
        binding?.rvFavoriteTracks?.adapter = adapter

        favoriteTracksViewModel.tracks.observe(viewLifecycleOwner) {
            adapter?.updateData(it)
            binding?.apply {
                layoutNoFavoriteTracks.isVisible = it.isEmpty()
                rvFavoriteTracks.isVisible = it.isNotEmpty()
            }
        }

        favoriteTracksViewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is FavoriteTracksScreenEvent.OpenPlayerScreen -> {
                    startActivity(Intent(requireContext(), PlayerActivity()::class.java))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        fun newInstance() = FavoriteTracksFragment()
    }
}