package com.practicum.playlistmaker.ui.search.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.search.model.Track

class SearchHistoryAdapter(): RecyclerView.Adapter<TrackViewHolder>() {
    //var tracks: MutableList<Track> = mutableListOf()
    var tracks = ArrayList<Track>()
    private var itemClickListener: TrackAdapter.ItemClickListener? = null
    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            itemClickListener?.onClick(track)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    interface ItemClickListener: TrackAdapter.ItemClickListener{
        override fun onClick(item: Track)
    }
    fun setItemClickListener(listener: ItemClickListener) {
        itemClickListener = listener
    }

}