package com.practicum.playlistmaker.domain.player.model

data class PlayerProgress(
    val status: Int,
    val position: Int = 0,
)