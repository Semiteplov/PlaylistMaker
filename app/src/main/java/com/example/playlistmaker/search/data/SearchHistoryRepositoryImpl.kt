package com.example.playlistmaker.search.data

import android.content.SharedPreferences
import com.example.playlistmaker.search.domain.api.SearchHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.TrackAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) :
    SearchHistoryRepository {

    companion object {
        private const val KEY_SEARCH_HISTORY = "key_search_history"
        private const val MAX_HISTORY_SIZE = 10
    }

    override fun saveSearchHistory(track: Track) {
        val json = sharedPreferences.getString(KEY_SEARCH_HISTORY, null)
        val currentHistory: MutableList<Track> =
            gson.fromJson(json, object : TypeToken<MutableList<Track>>() {}.type) ?: mutableListOf()

        currentHistory.removeAll { it.trackId == track.trackId }

        currentHistory.add(0, track)

        while (currentHistory.size > MAX_HISTORY_SIZE) {
            currentHistory.removeAt(currentHistory.lastIndex)
        }

        val jsonHistory = gson.toJson(currentHistory)
        sharedPreferences.edit()
            .putString(KEY_SEARCH_HISTORY, jsonHistory)
            .apply()
    }

    override fun getSearchHistory(): List<Track> {
        val json = sharedPreferences.getString(KEY_SEARCH_HISTORY, null)
        return gson.fromJson(json, object : TypeToken<MutableList<Track>>() {}.type)
            ?: mutableListOf()
    }

    override fun clearSearchHistory() {
        sharedPreferences.edit()
            .remove(KEY_SEARCH_HISTORY)
            .apply()
    }

    override fun saveTrackToHistory(track: Track) {
        val trackJson = gson.toJson(track)
        sharedPreferences.edit()
            .putString(TrackAdapter.NEW_TRACK_KEY, trackJson)
            .apply()
    }
}