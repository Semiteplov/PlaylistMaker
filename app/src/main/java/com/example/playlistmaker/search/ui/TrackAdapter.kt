package com.example.playlistmaker.search.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.media.ui.activity.MediaActivity
import com.example.playlistmaker.utils.Debouncer
import com.google.gson.Gson

class TrackAdapter(
    private val saveTrack: (Track) -> Unit,
    private val onTrackClickListener: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {
    companion object {
        const val NEW_TRACK_KEY = "new_track"
    }

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
            if (Debouncer.clickDebounce()) {
                saveTrack(track)
                onTrackClickListener(track)
                val trackJson = Gson().toJson(track)
                val intent = Intent(holder.itemView.context, MediaActivity::class.java).apply {
                    putExtra("key", trackJson)
                }
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = tracks.size

    fun set(newListTrack: List<Track>) {
        tracks = newListTrack
    }


    fun updateTracks(newTracks: List<Track>) {
        val diffCallback = TrackDiffCallback(tracks, newTracks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        tracks = newTracks
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}

class TrackDiffCallback(
    private val oldList: List<Track>,
    private val newList: List<Track>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].trackId == newList[newItemPosition].trackId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}