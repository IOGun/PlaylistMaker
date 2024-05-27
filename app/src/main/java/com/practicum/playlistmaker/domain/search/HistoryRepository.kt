package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.model.Track

interface HistoryRepository {
    fun addToHistory(track: Track)
    fun loadHistory(): ArrayList<Track>
    fun saveHistory(history: ArrayList<Track>)
    fun clearSearchHistory()
}