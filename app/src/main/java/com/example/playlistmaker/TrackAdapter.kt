package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(private val searchHistory: SearchHistory) : RecyclerView.Adapter<TrackViewHolder>() {
    var tracks: List<Track> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_track_list, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]

        holder.bind(track)
        holder.itemView.setOnClickListener {
            searchHistory.writeSearchHistory(track)
            Toast.makeText(holder.itemView.context, "Трек сохранен в истории", Toast.LENGTH_SHORT).show()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = tracks.size

    fun set(newListTrack: List<Track>) {
        tracks = newListTrack
    }

    fun clear() {
        tracks = emptyList()
    }
}