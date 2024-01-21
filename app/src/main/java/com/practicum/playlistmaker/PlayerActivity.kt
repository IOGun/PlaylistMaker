package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    companion object{
        private const val INTENT_KEY = "track"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<ImageView>(R.id.backArrow)
        backButton.setOnClickListener {
            finish()
        }

        val trackCover = findViewById<ImageView>(R.id.trackCover)
        val artistView = findViewById<TextView>(R.id.artistNameView)
        val collectionName = findViewById<TextView>(R.id.collectionNameView)
        val trackTimeMillis = findViewById<TextView>(R.id.trackTimeMillsView)
        val trackName = findViewById<TextView>(R.id.trackNameView)
        val country = findViewById<TextView>(R.id.countryView)
        val genre = findViewById<TextView>(R.id.genreView)
        val year = findViewById<TextView>(R.id.yearView)

        val dat = intent.getStringExtra(INTENT_KEY)
        val json = Gson()
        val track = json.fromJson(dat, Track::class.java)


        trackName.text = track.trackName
        artistView.text = track.artistName
        collectionName.text = track.collectionName
        trackTimeMillis.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
        country.text = track.country
        genre.text = track.primaryGenreName
        year.text = track.releaseDate.substring(0, 4)

        Glide.with(trackCover)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .centerCrop()
            .transform(RoundedCorners(8))
            .placeholder(R.drawable.cover_placeholder)
            .into(trackCover)
    }
}