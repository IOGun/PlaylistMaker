package com.practicum.playlistmaker.presentation.player

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.models.PlayerProgress
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val INTENT_KEY = "track"
        private const val EMPTY = ""
        private const val DEFAULT_TIMER_TEXT = "00:00"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY_TIMER = 300L
        private const val DP_VALUE = 8f
    }

    private lateinit var binding: ActivityPlayerBinding
    private var playerInteractor = Creator.providePlayerInteractor() //

    private val timerHandler = Handler(Looper.getMainLooper())

    private lateinit var playerState: PlayerProgress
    private var trackUrl: String? = EMPTY
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val dat = intent.getStringExtra(INTENT_KEY)
        val json = Gson()
        val track = json.fromJson(dat, Track::class.java)

        binding.backArrow.setOnClickListener { finish() }

        binding.trackNameView.text = track.trackName
        binding.artistNameView.text = track.artistName
        binding.collectionNameView.text = track.collectionName
        binding.trackTimeMillsView.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        binding.countryView.text = track.country
        binding.genreView.text = track.primaryGenreName
        binding.yearView.text = track.releaseDate.substring(0, 4)
        trackUrl = track.previewUrl

        binding.playButton.setOnClickListener {
            playbackControl()
        }

        val radius = convertDpToPx(DP_VALUE)

        Glide.with(binding.trackCover)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.cover_placeholder)
            .into(binding.trackCover)

        preparePlayer(track)
        //playerInteractor.preparePlayer(track)
        //playerState = playerInteractor.getPlayerState()
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.destroyPlayer()
        timerHandler.removeCallbacks(trackTimer)
    }

    private fun convertDpToPx(dp: Float): Int {
        return (dp * resources.displayMetrics.density + 0.5f).toInt()
    }

    private fun preparePlayer(track: Track) {
        playerInteractor.preparePlayer(track)
        playerState = playerInteractor.getPlayerState()
    }

    private fun playbackControl() {
        when (playerState.status) {
            STATE_PREPARED, STATE_PAUSED, STATE_DEFAULT -> {
                startPlayer()
            }

            STATE_PLAYING -> {
                pausePlayer()
            }

        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        playerState = playerInteractor.getPlayerState()
        timerHandler.post(trackTimer)
        binding.playButton.setImageResource(R.drawable.pause_button)
    }

    private fun pausePlayer() {
        playerInteractor.pausePlayer()
        playerState = playerInteractor.getPlayerState()
        binding.playButton.setImageResource(R.drawable.play_button)
    }

    private val dateFormat by lazy {
        SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
    }

    private var trackTimer = object : Runnable {
        override fun run() {
            playerState = playerInteractor.getPlayerState()
            when (playerState.status) {
                STATE_PLAYING -> {
                    timerHandler.postDelayed(this, DELAY_TIMER)
                    binding.timerView.text = dateFormat.format(playerState.position.toLong())
                }
                STATE_PAUSED -> {
                    timerHandler.removeCallbacks(this)
                }
                else -> {
                    timerHandler.removeCallbacks(this)
                    binding.timerView.text = dateFormat.format(0)
                    binding.playButton.setImageResource(R.drawable.play_button)
                }
            }
        }
    }


    private fun timer() {
        timerHandler.post(trackTimer)
    }
}