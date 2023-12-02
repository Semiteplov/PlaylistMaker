package com.example.playlistmaker.utils

import android.os.Handler
import android.os.Looper

object Debouncer {
    private const val CLICK_DEBOUNCE_DELAY_MILLIS = 500L
    private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }

    fun requestDebounce(request: () -> Unit) {
        val runnableRequest = Runnable(request)
        handler.removeCallbacks(runnableRequest)
        handler.postDelayed(runnableRequest, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }
}