package com.practicum.playlistmaker.ui.player.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
//import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityPlayerBinding
import com.practicum.playlistmaker.domain.player.model.PlayerProgress
import com.practicum.playlistmaker.domain.player.model.PlayerStatus
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object {
        private const val INTENT_KEY = "track"
        private const val EMPTY = ""
        private const val DP_VALUE = 8f
    }

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()
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
            viewModel.playbackControl()
        }

        val radius = convertDpToPx(DP_VALUE)

        Glide.with(binding.trackCover)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.cover_placeholder)
            .into(binding.trackCover)

        viewModel.preparePlayer(track)

        viewModel.playersProgress.observe(this) { playerProgress ->
            playbackControl(playerProgress)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.destroyPlayer()
    }

    private fun convertDpToPx(dp: Float): Int {
        return (dp * resources.displayMetrics.density + 0.5f).toInt()
    }



    private fun playbackControl(playerStatus: PlayerProgress) {
        when (playerStatus.status) {
            PlayerStatus.STATE_PLAYING -> {
                binding.timerView.text = dateFormat.format(playerStatus.position.toLong())
                binding.playButton.setImageResource(R.drawable.pause_button)
            }
            PlayerStatus.STATE_PREPARED, PlayerStatus.STATE_PAUSED -> {
                binding.playButton.setImageResource(R.drawable.play_button)
            }
            PlayerStatus.STATE_DEFAULT -> {
                binding.playButton.setImageResource(R.drawable.play_button)
            }
        }
    }



    private val dateFormat by lazy {
        SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        )
    }

}