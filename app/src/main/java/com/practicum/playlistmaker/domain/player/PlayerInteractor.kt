package com.practicum.playlistmaker.domain.player

import com.practicum.playlistmaker.domain.player.model.PlayerProgress
import com.practicum.playlistmaker.domain.search.model.Track

interface PlayerInteractor {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun destroyPlayer()
    fun getPlayerState(): PlayerProgress
}