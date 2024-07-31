package com.practicum.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.model.PlayerProgress
import com.practicum.playlistmaker.domain.player.model.PlayerStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var job: Job? = null

    private val privPlayersProgress: MutableLiveData<PlayerProgress> =
        MutableLiveData(updatePlayerProgress())

    val playersProgress: LiveData<PlayerProgress> get() = privPlayersProgress

    fun preparePlayer(track: Track) {
        playerInteractor.preparePlayer(track)
        privPlayersProgress.value = updatePlayerProgress()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        job?.cancel()
    }

    fun startPlayer() {
        playerInteractor.startPlayer()
        job?.cancel()
        job = viewModelScope.launch {
            while (true) {
                privPlayersProgress.value = updatePlayerProgress()
                when (privPlayersProgress.value!!.status) {
                    PlayerStatus.STATE_PLAYING -> {
                        delay(300L)
                    }
                    PlayerStatus.STATE_PAUSED -> {
                        break
                    }
                    else -> {
                        break
                    }
                }
            }
        }
    }

    fun destroyPlayer() {
        playerInteractor.destroyPlayer()
        job?.cancel()
    }

    fun playbackControl() {
        privPlayersProgress.value = updatePlayerProgress()
        when (privPlayersProgress.value!!.status) {
            PlayerStatus.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_PAUSED, PlayerStatus.STATE_DEFAULT -> {
                startPlayer()
            }
        }
    }

    private fun updatePlayerProgress(): PlayerProgress {
        return playerInteractor.getPlayerState()
    }

}