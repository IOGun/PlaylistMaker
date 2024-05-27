package com.practicum.playlistmaker.domain.models

data class TrackSearchResult(
    val tracks: List<Track>,
    var status: AppCollectionStatus = AppCollectionStatus.DEFAULT
)
