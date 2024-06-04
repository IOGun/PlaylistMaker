package com.practicum.playlistmaker.domain.search.model

import com.practicum.playlistmaker.domain.player.model.AppCollectionStatus
import com.practicum.playlistmaker.domain.search.model.Track

data class TrackSearchResult(
    val tracks: List<Track>,
    var status: Status = Status.DEFAULT
)
