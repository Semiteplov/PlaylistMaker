package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTitle: TextView = itemView.findViewById(R.id.track_title)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val artworkImage: ImageView = itemView.findViewById(R.id.artwork_image)

    private companion object {
        const val radiusCorners = 2.0f
    }

    fun bind(track: Track) {
        val requestOptions = RequestOptions()
            .placeholder(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        trackTitle.text = track.trackTitle
        artistName.text = track.artistName
        trackTime.text = track.getFormattedTime()
        Glide.with(itemView)
            .applyDefaultRequestOptions(requestOptions)
            .load(track.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(dpToPx(radiusCorners, itemView.context)))
            .into(artworkImage)
    }

    private fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
        ).toInt()
    }
}