package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.AppCollectionStatus
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.models.TrackSearchResult


interface TrackRepository {
    fun searchTrack(expression: String): TrackSearchResult
}