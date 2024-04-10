package com.example.playlistmaker.media.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.media.data.PlayerState
import com.example.playlistmaker.media.ui.adapters.BottomSheetPlaylistAdapter
import com.example.playlistmaker.media.ui.events.PlayerScreenEvent
import com.example.playlistmaker.media.ui.utils.ResultKeyHolder
import com.example.playlistmaker.media.ui.view_model.PlayerViewModel
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.DateFormatter
import com.example.playlistmaker.utils.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerFragment : Fragment() {

    private var binding: FragmentPlayerBinding? = null
    private val viewModel: PlayerViewModel by viewModel()

    private lateinit var playlistAdapter: BottomSheetPlaylistAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlayerBinding.inflate(layoutInflater)
        requireActivity().supportFragmentManager.setFragmentResultListener(
            ResultKeyHolder.KEY_PLAYLIST_CREATED,
            viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(ResultKeyHolder.KEY_PLAYLIST_NAME)?.let {
                val text = getString(R.string.playlist_created_snackbar, it)
                showToast(text)
            }
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPlaylistsBottomSheet()
        initButtons()
        initObservers()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initButtons() {
        binding?.apply {
            toolbarMedia.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
            ibAdd.setOnClickListener { viewModel.onAddButtonClicked() }
            ibLike.setOnClickListener { viewModel.onLikeButtonClicked() }
            ibPlay.setOnClickListener { viewModel.onPlayButtonClicked() }
            btnCreatePlaylist.setOnClickListener { viewModel.onCreatePlaylistButtonClicked() }
        }
    }

    private fun initPlaylistsBottomSheet() {
        binding?.let {
            BottomSheetBehavior.from(it.bottomSheetPlaylists).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
                addBottomSheetCallback(
                    object : BottomSheetBehavior.BottomSheetCallback() {

                        override fun onStateChanged(bottomSheet: View, newState: Int) {
                            it.overlay.isVisible = newState != BottomSheetBehavior.STATE_HIDDEN
                        }

                        override fun onSlide(bottomSheet: View, slideOffset: Float) {}
                    })
            }
            playlistAdapter = BottomSheetPlaylistAdapter(viewModel::onPlaylistClicked)
            it.rvPlaylists.adapter = playlistAdapter
        }
    }

    private fun initObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            it.track?.let { track -> setTrackData(track) }
            binding?.tvTimeTrack?.text = it.trackTime.ifEmpty {
                getString(R.string.default_track_time)
            }
            it?.playerState?.let { state ->
                when (state) {
                    PlayerState.STATE_PLAYING -> binding?.ibPlay?.setImageResource(R.drawable.ic_pause_button)
                    PlayerState.STATE_PREPARED,
                    PlayerState.STATE_PAUSED -> binding?.ibPlay?.setImageResource(R.drawable.ic_play_button)

                    else -> {}
                }
            }
            if (it.isFavoriteTrack)
                binding?.ibLike?.setImageResource(R.drawable.ic_liked)
            else
                binding?.ibLike?.setImageResource(R.drawable.ic_like)
        }

        viewModel.playlists.observe(viewLifecycleOwner) { playlistAdapter.submitList(it) }

        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is PlayerScreenEvent.OpenPlaylistsBottomSheet -> {
                    binding?.bottomSheetPlaylists?.let {
                        val bottomSheetBehavior = BottomSheetBehavior.from(it)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                is PlayerScreenEvent.ShowTrackAddedMessage -> {
                    showToast(getString(R.string.added_to_playlist, it.playlistName))
                }

                is PlayerScreenEvent.ShowTrackAlreadyInPlaylistMessage -> {
                    showToast(getString(R.string.track_already_in_playlist, it.playlistName))
                }

                is PlayerScreenEvent.ClosePlaylistsBottomSheet -> {
                    binding?.bottomSheetPlaylists?.let {
                        val bottomSheetBehavior = BottomSheetBehavior.from(it)
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                }

                is PlayerScreenEvent.NavigateToCreatePlaylistScreen -> {
                    findNavController().navigate(R.id.action_playerFragment_to_newPlaylistFragment)
                }

            }
        }
    }

    private fun setTrackData(track: Track) {
        Log.d("PlayerFragment", "setTrackData: $track")
        binding?.apply {
            tvArtistName.text = track.artistName
            tvTrackName.text = track.trackTitle
            if (track.collectionName.isNotEmpty())
                tvAlbumValue.text = track.collectionName
            else {
                tvAlbumValue.isVisible = false
                tvAlbum.isVisible = false
            }
            tvCountryValue.text = track.country
            tvYearValue.text = DateFormatter.getYearFromReleaseDate(track.releaseDate)
            tvDurationValue.text =
                DateFormatter.formatMillisToString(track.trackTime).replaceFirst("0", "")
            tvGenreValue.text = track.primaryGenreName
            ivMain.load(track.artworkUrl100, true)
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}