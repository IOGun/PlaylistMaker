package com.practicum.playlistmaker.ui.search.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.search.model.Track

class SearchHistoryAdapter(): RecyclerView.Adapter<TrackViewHolder>() {

    var tracks = ArrayList<Track>()
    var itemClickListener: TrackAdapter.OnItemClickListener? = null

    interface OnItemClickListener : TrackAdapter.OnItemClickListener {
        override fun onItemClick(track: Track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder =
        TrackViewHolder(parent)


    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(track)
        }
    }

    override fun getItemCount(): Int = tracks.size

}