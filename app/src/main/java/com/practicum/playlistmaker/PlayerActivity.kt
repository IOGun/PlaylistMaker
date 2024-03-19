package com.practicum.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
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
    private val handler = Handler(Looper.getMainLooper())
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
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

        binding.playButton.setOnClickListener { playbackControl() }

        val radius = convertDpToPx(DP_VALUE)

        Glide.with(binding.trackCover)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.cover_placeholder)
            .into(binding.trackCover)

        preparePlayer()
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun convertDpToPx(dp: Float): Int {
        return (dp * resources.displayMetrics.density + 0.5f).toInt()
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(trackUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.playButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.playButton.setImageResource(R.drawable.play_button)
            playerState = STATE_PREPARED
            binding.timerView.text = DEFAULT_TIMER_TEXT
            handler.removeCallbacks(trackTimer)
        }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }

            STATE_PLAYING -> {
                pausePlayer()
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
        timer()
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        binding.playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
        handler.removeCallbacks(trackTimer)
    }

    private val dateFormat by lazy {
        SimpleDateFormat(
            "mm:ss",
            Locale.getDefault())
    }

    private var trackTimer = object : Runnable {
        override fun run() {
            binding.timerView.text = dateFormat.format(mediaPlayer.currentPosition.toLong())
            handler.postDelayed(this, DELAY_TIMER)
        }
    }


    private fun timer() {
        handler.post(trackTimer)
    }
}