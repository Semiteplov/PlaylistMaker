package com.example.playlistmaker.media.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ItemTrackListBinding
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.utils.DateFormatter
import com.example.playlistmaker.utils.load

class FavoriteTracksAdapter(
    private val onTrackClicked: (track: Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    private var tracks = emptyList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemTrackListBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = tracks[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onTrackClicked(item) }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newListTrack: List<Track>) {
        tracks = newListTrack
        notifyDataSetChanged()
    }
}

class TrackViewHolder(
    private val binding: ItemTrackListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        with(binding) {
            artistName.text = track.artistName
            trackTitle.text = track.trackTitle
            trackTime.text = DateFormatter.formatMillisToString(track.trackTime)
            artworkImage.load(track.artworkUrl100)
        }
    }
}
