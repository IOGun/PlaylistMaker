package com.practicum.playlistmaker

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val sourceTrackName = itemView.findViewById<TextView>(R.id.trackName)
    private val sourceArtistName = itemView.findViewById<TextView>(R.id.artistName)
    private val sourceTrackTime = itemView.findViewById<TextView>(R.id.trackTime)
    private val image = itemView.findViewById<ImageView>(R.id.trackImage)

    fun bind(model: Track) {
        sourceTrackName.text = model.trackName
        sourceArtistName.text = model.artistName
        sourceTrackTime.text = model.trackTime
        Glide.with(image)
            .load(model.artworkUrl100)
            .centerCrop()
            .transform(RoundedCorners(2))
            .placeholder(R.drawable.placeholder)
            .into(image)
    }
}