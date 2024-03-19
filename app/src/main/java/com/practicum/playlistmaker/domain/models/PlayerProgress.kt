package com.practicum.playlistmaker.domain.models

data class PlayerProgress(
    val status: Int,
    val position: Int = 0,
)