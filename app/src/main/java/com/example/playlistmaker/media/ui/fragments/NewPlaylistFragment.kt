package com.example.playlistmaker.media.ui.fragments

import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.blue
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.media.ui.events.NewPlaylistEvent
import com.example.playlistmaker.media.ui.utils.ResultKeyHolder
import com.example.playlistmaker.media.ui.view_model.NewPlaylistViewModel
import com.example.playlistmaker.utils.load
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {

    private var binding: FragmentNewPlaylistBinding? = null
    private val viewModel: NewPlaylistViewModel by viewModel()
    private val pickMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {
        it?.let { uri ->
            viewModel.onPlaylistCoverSelected(uri)
            setSelectedCover(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        setupTextListeners()
        setupClickListeners()
        setupBackPressHandling()
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupTextListeners() {
        binding?.apply {
            editTextName.doAfterTextChanged { viewModel.onPlaylistNameChanged(it.toString()) }
            editTextDescription.doAfterTextChanged {
                viewModel.onPlaylistDescriptionChanged(it.toString())
            }
        }
    }

    private fun setupClickListeners() {
        binding?.apply {
            ibCover.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            btnCreate.setOnClickListener { viewModel.onCreatePlaylistClicked() }
        }
    }

    private fun setupBackPressHandling() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    viewModel.onBackPressed()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun initObservers() {
        viewModel.isButtonCreateEnabled.observe(viewLifecycleOwner) {
            binding?.btnCreate?.isEnabled = it
        }
        viewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is NewPlaylistEvent.NavigateBack -> findNavController().popBackStack()
                is NewPlaylistEvent.ShowBackConfirmationDialog -> showBackConfirmationDialog()
                is NewPlaylistEvent.SetPlaylistCreatedResult -> {
                    requireActivity().supportFragmentManager.setFragmentResult(
                        ResultKeyHolder.KEY_PLAYLIST_CREATED,
                        bundleOf(ResultKeyHolder.KEY_PLAYLIST_NAME to it.playlistName)
                    )
                }
            }
        }
    }

    private fun showBackConfirmationDialog() {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.confirmation_dialog_title))
            .setMessage(getString(R.string.confirmation_dialog_message))
            .setNegativeButton(getString(R.string.confirmation_dialog_negative)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.confirmation_dialog_positive)) { _, _ ->
                viewModel.onBackPressedConfirmed()
            }
            .show()
        dialog.getButton(BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.blue))
        dialog.getButton(BUTTON_NEGATIVE).setTextColor(resources.getColor(R.color.blue))
    }

    private fun setSelectedCover(uri: Uri) {
        binding?.ibCover?.load(uri.toString())
    }
}