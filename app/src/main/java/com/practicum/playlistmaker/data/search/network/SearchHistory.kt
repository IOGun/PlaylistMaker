package com.practicum.playlistmaker.data.search.network

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.domain.search.model.Track

class SearchHistory(
    private val sharedPreference: SharedPreferences
) {

    companion object {
        const val SEARCH_HISTORY_KEY = "key_for_track"
    }


    fun read(): MutableList<Track> {
        val json = sharedPreference.getString(SEARCH_HISTORY_KEY, null) ?: return mutableListOf()
        return Gson().fromJson(json, Array<Track>::class.java).toMutableList()
    }

    fun write(history: MutableList<Track>): Unit {
        val json = Gson().toJson(history)
        sharedPreference.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }

    fun clear() {
        sharedPreference.edit()
            .clear()
            .apply()
    }
}