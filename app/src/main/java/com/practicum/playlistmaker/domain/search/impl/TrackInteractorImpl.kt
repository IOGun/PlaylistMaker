package com.practicum.playlistmaker.domain.search.impl

import com.practicum.playlistmaker.domain.search.TrackInteractor
import com.practicum.playlistmaker.domain.search.TrackRepository
import com.practicum.playlistmaker.domain.search.model.TrackSearchResult
import kotlinx.coroutines.flow.Flow

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {
    override suspend fun searchTrack(expression: String): Flow<TrackSearchResult> {
        return repository.searchTrack(expression)
    }

}