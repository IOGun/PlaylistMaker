package com.practicum.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale


class TrackViewHolder(parent:ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
) {
    private val sourceTrackName = itemView.findViewById<TextView>(R.id.trackName)
    private val sourceArtistName = itemView.findViewById<TextView>(R.id.artistName)
    private val sourceTrackTime = itemView.findViewById<TextView>(R.id.trackTime)
    private val image = itemView.findViewById<ImageView>(R.id.trackImage)

    fun bind(model: Track) {
        sourceTrackName.text = model.trackName
        sourceArtistName.text = model.artistName
        sourceTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTimeMillis)
        Glide.with(image)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(image)
    }
}