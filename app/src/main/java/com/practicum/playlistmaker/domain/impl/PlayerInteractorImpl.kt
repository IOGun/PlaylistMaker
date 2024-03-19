package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerRepository
import com.practicum.playlistmaker.domain.models.PlayerProgress
import com.practicum.playlistmaker.domain.models.Track

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun preparePlayer(track: Track) {
        playerRepository.preparePlayer(track)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun destroyPlayer() {
        playerRepository.destroyPlayer()
    }

    override fun getPlayerState(): PlayerProgress {
        return playerRepository.getPlayerState()
    }
}