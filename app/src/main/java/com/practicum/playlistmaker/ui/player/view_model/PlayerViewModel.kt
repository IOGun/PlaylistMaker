package com.practicum.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.model.PlayerProgress

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private val progressHandler = Handler(Looper.getMainLooper())

    private val privPlayersProgress: MutableLiveData<PlayerProgress> =
        MutableLiveData(updatePlayerProgress())

    val playersProgress: LiveData<PlayerProgress> get() = privPlayersProgress

    fun preparePlayer(track: Track) {
        playerInteractor.preparePlayer(track)
        privPlayersProgress.value = updatePlayerProgress()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        privPlayersProgress.value = updatePlayerProgress()
    }

    fun startPlayer() {
        playerInteractor.startPlayer()
        progressHandler.post(updatePlayerProgressRunnable())
        privPlayersProgress.value = updatePlayerProgress()
    }

    fun destroyPlayer() {
        progressHandler.removeCallbacks(updatePlayerProgressRunnable())
        playerInteractor.destroyPlayer()
    }

    fun playbackControl() {
        privPlayersProgress.value = updatePlayerProgress()
        when (privPlayersProgress.value!!.status) {
            STATE_PLAYING -> {
                playerInteractor.pausePlayer()
                privPlayersProgress.value = updatePlayerProgress()
            }
            STATE_PREPARED, STATE_PAUSED, STATE_DEFAULT -> {
                playerInteractor.startPlayer()
                progressHandler.post(updatePlayerProgressRunnable())
                privPlayersProgress.value = updatePlayerProgress()
            }
        }
    }

    private fun updatePlayerProgress(): PlayerProgress {
        return playerInteractor.getPlayerState()
    }

    private fun updatePlayerProgressRunnable(): Runnable {
        return object : Runnable {
            override fun run() {
                privPlayersProgress.value = updatePlayerProgress()
                when (privPlayersProgress.value!!.status) {
                    STATE_PLAYING -> {
                        progressHandler.postDelayed(this, 300)
                    }

                    STATE_PAUSED -> {
                        progressHandler.removeCallbacks(this)
                    }

                    else -> {
                        progressHandler.removeCallbacks(this)
                    }
                }

            }
        }
    }


}