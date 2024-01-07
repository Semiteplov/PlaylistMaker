package com.example.playlistmaker.media.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.util.Locale

object Player {
    private const val TAG = "Player"

    private var state = PlayerState.STATE_DEFAULT
    private var mediaPlayer: MediaPlayer? = MediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private var timerRunnable: Runnable? = null

    private const val TIMER_TICK_MILLIS = 200L

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
        timerRunnable = object : Runnable {
            override fun run() {
                if (state == PlayerState.STATE_PLAYING) {
                    val currentPosition = mediaPlayer?.currentPosition ?: 0
                    val minutes = (currentPosition / 1000) / 60
                    val seconds = (currentPosition / 1000) % 60
                    val formattedTime =
                        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

                    setTimerListener(formattedTime)
                    handler.postDelayed(this, TIMER_TICK_MILLIS)
                }
            }
        }
        timerRunnable?.run()
    }

    fun stopTimer() {
        timerRunnable?.let { handler.removeCallbacks(it) }
    }
}