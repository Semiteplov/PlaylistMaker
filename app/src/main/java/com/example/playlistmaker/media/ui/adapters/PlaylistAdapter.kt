package com.example.playlistmaker.media.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.media.domain.models.Playlist
import com.example.playlistmaker.utils.load

class PlaylistAdapter :
    ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder>(PlaylistDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(ItemPlaylistBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PlaylistViewHolder(
        private val binding: ItemPlaylistBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist) {
            with(binding) {
                playlist.coverUri?.let { ivCover.load(it) }
                textName.text = playlist.name
                textTracksCount.text = getTracksCountText(playlist.tracksCount)
            }
        }

        private fun getTracksCountText(tracksCount: Int): String {
            val pluralsString = binding.root.resources.getQuantityString(
                R.plurals.track,
                tracksCount,
                tracksCount
            )
            return "$tracksCount $pluralsString"
        }
    }

    private class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {

        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist) =
            oldItem == newItem
    }
}