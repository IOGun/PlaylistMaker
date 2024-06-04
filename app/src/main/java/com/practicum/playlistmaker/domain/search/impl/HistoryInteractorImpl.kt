package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.search.HistoryInteractor
import com.practicum.playlistmaker.domain.search.HistoryRepository
import com.practicum.playlistmaker.domain.search.model.Track

class HistoryInteractorImpl(private val trackHistoryRepository: HistoryRepository) :
    HistoryInteractor {

    override fun addToHistory(track: Track) {
        trackHistoryRepository.addToHistory(track)
    }

    override fun loadHistory(): ArrayList<Track> {
        return trackHistoryRepository.loadHistory()
    }

    override fun saveHistory(history: ArrayList<Track>) {
        trackHistoryRepository.saveHistory(history)
    }

    override fun clearSearchHistory() {
        trackHistoryRepository.clearSearchHistory()
    }

}