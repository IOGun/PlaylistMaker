package com.practicum.playlistmaker.ui.mediateka.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.ui.mediateka.fragment.PlaylistFragment
import com.practicum.playlistmaker.ui.mediateka.fragment.TopTracksFragment

class MediatekaViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        private const val FRAGMENTS = 2
    }

    override fun getItemCount(): Int {
        return FRAGMENTS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TopTracksFragment.newInstance()
            else -> PlaylistFragment.newInstance()
        }
    }
}