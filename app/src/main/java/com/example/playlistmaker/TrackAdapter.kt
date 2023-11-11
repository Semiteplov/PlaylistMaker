package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(
    private val searchHistory: SearchHistory, private val onTrackClickListener: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {
    var tracks: List<Track> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_track_list, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]

        holder.bind(track)
        holder.itemView.setOnClickListener {
            onTrackClickListener(track)
        }
    }

    override fun getItemCount() = tracks.size

    fun set(newListTrack: List<Track>) {
        tracks = newListTrack
    }

    fun clear() {
        tracks = emptyList()
    }

    fun updateTracks(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }
}