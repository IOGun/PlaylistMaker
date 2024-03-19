package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.domain.models.PlayerProgress
import com.practicum.playlistmaker.domain.models.Track

interface PlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun destroyPlayer()
    fun getPlayerState(): PlayerProgress
}