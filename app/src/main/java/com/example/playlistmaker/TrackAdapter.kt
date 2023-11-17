package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackAdapter(
    private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val onTrackClickListener: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    var tracks: List<Track> = emptyList()
    private val newTrackKey = "new_track"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_track_list, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]

        holder.bind(track)
        holder.itemView.setOnClickListener {
            write(track)
            onTrackClickListener(track)
            val trackJson = Gson().toJson(track)
            val intent = Intent(context, MediaActivity::class.java).apply {
                putExtra("key", trackJson)
            }
            context.startActivity(intent)
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

    private fun write(track: Track) {
        sharedPreferences.edit()
            .putString(newTrackKey, Gson().toJson(track))
            .apply()
    }
}
