package com.example.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val gson: Gson = GsonBuilder().create()

    fun clearSearchHistory() {
        sharedPreferences.edit()
            .remove(KEY_SEARCH_HISTORY)
            .apply()
    }

    fun readSearchHistory(): MutableList<Track> {
        val json = sharedPreferences.getString(KEY_SEARCH_HISTORY, null)
        return gson.fromJson(json, object : TypeToken<MutableList<Track>>() {}.type)
            ?: mutableListOf()
    }

    fun writeSearchHistory(track: Track) {
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

    companion object {
        const val KEY_SEARCH_HISTORY = "key_search_history"
        const val MAX_HISTORY_SIZE = 10
    }
}
