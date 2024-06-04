package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.player.model.PlayerProgress
import com.practicum.playlistmaker.domain.player.model.PlayerStatus
import com.practicum.playlistmaker.domain.search.model.Track

class PlayerRepositoryImpl(
    private var mediaPlayer: MediaPlayer,
    private var playerState: PlayerStatus = PlayerStatus.STATE_DEFAULT
) : PlayerRepository {
    /*
    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }*/

    //private var mediaPlayer = MediaPlayer()
    //private var playerState = STATE_DEFAULT

    override fun preparePlayer(track: Track) {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerStatus.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerStatus.STATE_DEFAULT
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = PlayerStatus.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = PlayerStatus.STATE_PAUSED
    }

    override fun destroyPlayer() {
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
    }

    override fun getPlayerState(): PlayerProgress {
        val currentPosition = mediaPlayer.currentPosition
        return when (playerState) {
            PlayerStatus.STATE_PLAYING -> {
                PlayerProgress(
                    status = PlayerStatus.STATE_PLAYING,
                    position = currentPosition
                )
            }

            PlayerStatus.STATE_PAUSED -> {
                PlayerProgress(
                    status = PlayerStatus.STATE_PAUSED,
                    position = currentPosition
                )
            }

            PlayerStatus.STATE_PREPARED -> {
                PlayerProgress(
                    status = PlayerStatus.STATE_PREPARED,
                    position = currentPosition
                )
            }

            PlayerStatus.STATE_DEFAULT -> {
                PlayerProgress(
                    status = PlayerStatus.STATE_DEFAULT,
                    position = currentPosition
                )
            }

            else -> {
                PlayerProgress(
                    status = PlayerStatus.STATE_DEFAULT,
                    position = 0
                )
            }
        }

    }
}