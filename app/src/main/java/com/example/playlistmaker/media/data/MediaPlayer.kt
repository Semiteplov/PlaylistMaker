package com.example.playlistmaker.media.data

import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

object Player : CoroutineScope {
    private const val TAG = "Player"

    private var state = PlayerState.STATE_DEFAULT
    private var mediaPlayer: MediaPlayer? = MediaPlayer()

    private const val TIMER_TICK_MILLIS = 300L
    private var timerJob: Job? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + SupervisorJob()

    fun prepare(mediaUrl: String, onCompletionListener: () -> Unit) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
        mediaPlayer?.apply {
            reset()
            setDataSource(mediaUrl)
            prepareAsync()
            setOnPreparedListener {
                state = PlayerState.STATE_PREPARED
            }
            setOnCompletionListener {
                stopTimer()
                onCompletionListener()
                state = PlayerState.STATE_PREPARED
            }
            setOnErrorListener { _, _, _ ->
                state = PlayerState.STATE_ERROR
                true
            }
        }
    }

    private fun start(onStartListener: () -> Unit) {
        mediaPlayer?.start()
        onStartListener()
        state = PlayerState.STATE_PLAYING
    }

    fun pause(onPauseListener: () -> Unit) {
        mediaPlayer?.pause()
        onPauseListener()
        state = PlayerState.STATE_PAUSED
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        state = PlayerState.STATE_DEFAULT
        stopTimer()
    }

    fun playbackControl(listener: IMediaPlayerControlListener) {
        when (state) {
            PlayerState.STATE_PLAYING -> {
                pause { listener.onPausePlayer() }
                stopTimer()
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                start { listener.onStartPlayer() }
                startTimer { listener.onTimeUpdate(it) }
            }

            else -> {
                Log.w(TAG, "playbackControl called in unexpected state: $state")
            }
        }
    }

    private fun startTimer(setTimerListener: (String) -> Unit) {
        timerJob = launch {
            while (isActive && state == PlayerState.STATE_PLAYING) {
                val currentPosition = mediaPlayer?.currentPosition ?: 0
                val roundedPosition = currentPosition + 999
                val minutes = TimeUnit.MILLISECONDS.toMinutes(roundedPosition.toLong())
                val seconds = TimeUnit.MILLISECONDS.toSeconds(roundedPosition.toLong()) % 60
                val formattedTime =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

                setTimerListener(formattedTime)
                delay(TIMER_TICK_MILLIS)
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
    }
}