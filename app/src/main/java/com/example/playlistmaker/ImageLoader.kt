package com.example.playlistmaker

import android.content.Context
import android.util.TypedValue
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions

private const val BORDER_RADIUS = 2.0f

fun ImageView.loadTrackImage(
    context: Context,
    view: ImageView,
    track: Track,
    useLargeImage: Boolean
) {
    Glide.with(context)
        .load(track.getImageNeedSize())
        .apply(getRequestOptions(useLargeImage))
        .transform(CenterCrop(), RoundedCorners(dpToPx(BORDER_RADIUS, context)))
        .into(view)
}

private fun getRequestOptions(useLargeImage: Boolean): RequestOptions {
    return if (useLargeImage) {
        RequestOptions()
            .placeholder(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    } else {
        RequestOptions()
            .placeholder(R.drawable.placeholder)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }
}

private fun dpToPx(dp: Float, context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    ).toInt()
}
