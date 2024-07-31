package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.model.TrackSearchResult
import kotlinx.coroutines.flow.Flow


interface TrackInteractor {
       suspend fun searchTrack(expression: String): Flow<TrackSearchResult>
}