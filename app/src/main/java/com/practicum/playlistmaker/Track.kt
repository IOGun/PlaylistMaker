package com.practicum.playlistmaker


data class Track(
    val trackName: String = "",
    val artistName: String = "",
    val trackTimeMillis: Int = 0,
    val artworkUrl100: String = "",
    val trackId: Int = 0,
)
