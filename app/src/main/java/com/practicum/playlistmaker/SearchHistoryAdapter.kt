package com.practicum.playlistmaker

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryAdapter(): RecyclerView.Adapter<TrackViewHolder>() {
    var tracks: MutableList<Track> = mutableListOf()
    var itemClickListener: TrackAdapter.ItemClickListener? = null
    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val item = tracks[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    fun interface ItemClickListener{
        fun onClick(item: Track)
    }
}