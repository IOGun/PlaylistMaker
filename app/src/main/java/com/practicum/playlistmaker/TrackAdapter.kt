package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(val itemClickListener: ItemClickListener) : RecyclerView.Adapter<TrackViewHolder>() {
    var tracks: MutableList<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks.get(position))
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(tracks.get(position))
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
    fun interface ItemClickListener {
        fun onClick(item: Track)
    }
}