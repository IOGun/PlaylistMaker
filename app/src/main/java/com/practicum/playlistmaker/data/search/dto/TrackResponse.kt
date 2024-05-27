package com.practicum.playlistmaker.data.search.dto

class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>,
): Response()