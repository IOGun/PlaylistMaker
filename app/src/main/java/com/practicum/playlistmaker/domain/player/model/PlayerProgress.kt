package com.practicum.playlistmaker.domain.player.model

data class PlayerProgress(
    //val status: Int,
    val status: PlayerStatus,
    val position: Int = 0,
)