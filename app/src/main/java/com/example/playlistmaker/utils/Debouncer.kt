package com.example.playlistmaker.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

object Debouncer : CoroutineScope {
    private const val CLICK_DEBOUNCE_DELAY_MILLIS = 500L
    private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    private var clickJob: Job? = null
    private var searchJob: Job? = null

    fun clickDebounce(action: () -> Unit): Boolean {
        clickJob?.cancel()
        clickJob = launch {
            delay(CLICK_DEBOUNCE_DELAY_MILLIS)
            action()
        }
        return clickJob?.isActive == true
    }

    fun requestDebounce(request: () -> Unit) {
        searchJob?.cancel()
        searchJob = launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            request()
        }
    }
}