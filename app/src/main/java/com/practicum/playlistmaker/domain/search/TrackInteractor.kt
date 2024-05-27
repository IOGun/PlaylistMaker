package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.search.model.TrackSearchResult


interface TrackInteractor {
    fun searchTrack(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTrack: TrackSearchResult)
    }
}