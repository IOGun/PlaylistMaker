package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.player.model.PlayerProgress
import com.practicum.playlistmaker.domain.search.model.Track

class PlayerRepositoryImpl : PlayerRepository {
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_DEFAULT
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
    }

    override fun getPlayerState(): PlayerProgress {
        val currentPosition = mediaPlayer.currentPosition
        return when (playerState) {
            STATE_PLAYING -> {
                PlayerProgress(
                    status = STATE_PLAYING,
                    position = currentPosition
                )
            }

            STATE_PAUSED -> {
                PlayerProgress(
                    status = STATE_PAUSED,
                    position = currentPosition
                )
            }

            STATE_PREPARED -> {
                PlayerProgress(
                    status = STATE_PREPARED,
                    position = currentPosition
                )
            }

            STATE_DEFAULT -> {
                PlayerProgress(
                    status = STATE_DEFAULT,
                    position = currentPosition
                )
            }

            else -> {
                PlayerProgress(
                    status = STATE_DEFAULT,
                    position = 0
                )
            }
        }

    }
}