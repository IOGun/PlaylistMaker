package com.practicum.playlistmaker.data.search.Impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.search.HistoryRepository
import com.practicum.playlistmaker.domain.search.model.Track

class HistoryRepositoryImpl(val sharedPreferences: SharedPreferences, val gson: Gson) : HistoryRepository {

    companion object {
        private const val SEARCH_HISTORY_KEY = "key_for_track"
        private const val MAX_SIZE = 10
    }

   // private val gson = Gson()

    override fun addToHistory(track: Track) {
        val history = loadHistory()
        history.removeAll { it == track }
        history.add(0, track)
        if (history.size > MAX_SIZE) {
            history.removeAt(MAX_SIZE)
        }
        saveHistory(history)
    }

    override fun loadHistory(): ArrayList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<ArrayList<Track>>() {}.type
            gson.fromJson(json, type)
        } else {
            ArrayList()
        }
    }

    override fun saveHistory(history: ArrayList<Track>) {
        val saveOfHistory = if (history.size > MAX_SIZE) {
            ArrayList(history.subList(0, MAX_SIZE))
        } else {
            history
        }
        val json = gson.toJson(saveOfHistory)
        val editor = sharedPreferences.edit()
        editor.putString(SEARCH_HISTORY_KEY, json)
        editor.apply()
    }

    override fun clearSearchHistory() {
        val editor = sharedPreferences.edit()
        editor.remove(SEARCH_HISTORY_KEY)
        editor.apply()
    }
}