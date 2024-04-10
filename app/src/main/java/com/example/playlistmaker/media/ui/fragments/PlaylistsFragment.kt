package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media.ui.adapters.PlaylistAdapter
import com.example.playlistmaker.media.ui.events.PlaylistsScreenEvent
import com.example.playlistmaker.media.ui.utils.ResultKeyHolder
import com.example.playlistmaker.media.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private val viewModel: PlaylistsViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!
    private val playlistAdapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(layoutInflater, container, false)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            ResultKeyHolder.KEY_PLAYLIST_CREATED,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(ResultKeyHolder.KEY_PLAYLIST_NAME)
                ?.let { showPlaylistCreatedMessage(it) }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.playlists.observe(viewLifecycleOwner) {
            playlistAdapter.submitList(it)
            binding.layoutNoPlaylists.isVisible = it.isEmpty()
            binding.rvPlaylists.isVisible = it.isNotEmpty()
        }
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                PlaylistsScreenEvent.NavigateToNewPlaylist -> navigateToNewPlaylist()
            }
        }

        binding.btnNewPlaylist.setOnClickListener { viewModel.onNewPlaylistButtonClicked() }
        binding.rvPlaylists.adapter = playlistAdapter

        binding.btnNewPlaylist.visibility = View.VISIBLE
        binding.ivNoTracks.visibility = View.VISIBLE
        binding.tvNoPlaylists.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToNewPlaylist() {
        findNavController()
            .navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
    }

    private fun showPlaylistCreatedMessage(playlistName: String) {
        Toast.makeText(
            requireContext(),
            getString(R.string.playlist_created_snackbar, playlistName),
            Toast.LENGTH_SHORT
        )
            .show()
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }
}