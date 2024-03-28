package com.example.playlistmaker.media.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.media.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackViewHolder
import com.example.playlistmaker.utils.Debouncer
import com.google.gson.Gson

class FavoriteTracksAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    private var favoriteTracks: List<Track> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_track_list, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = favoriteTracks[position]

        holder.bind(track)
        holder.itemView.setOnClickListener {
            Debouncer.clickDebounce {
                val trackJson = Gson().toJson(track)
                val intent = Intent(holder.itemView.context, PlayerActivity::class.java).apply {
                    putExtra("key", trackJson)
                }
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = favoriteTracks.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTracks(newTracks: List<Track>) {
        val diffCallback = TrackDiffCallback(favoriteTracks, newTracks)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        favoriteTracks = newTracks
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