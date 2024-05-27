package com.practicum.playlistmaker.ui.search.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.search.model.Track

class TrackAdapter : RecyclerView.Adapter<TrackViewHolder>() {
   // var tracks: MutableList<Track> = mutableListOf()
    var tracks = ArrayList<Track>()
    private var clickOnItem: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            clickOnItem?.onClick(track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }
    interface ItemClickListener {
        fun onClick(item: Track)
    }

    fun setItemClickListener(listener: ItemClickListener) {
        clickOnItem = listener
    }
}