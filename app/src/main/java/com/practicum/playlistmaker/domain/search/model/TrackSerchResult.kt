package com.practicum.playlistmaker.domain.search.model

data class TrackSearchResult(
    val tracks: List<Track>,
    var status: Status = Status.DEFAULT
)
