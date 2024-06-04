package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.model.TrackSearchResult


interface TrackRepository {
    fun searchTrack(expression: String): TrackSearchResult
}