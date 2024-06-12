package com.practicum.playlistmaker.ui.mediateka.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityMediatekaBinding
import com.practicum.playlistmaker.ui.mediateka.adapter.MediatekaViewPagerAdapter

class MediatekaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediatekaBinding

    private lateinit var tabLayoutMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediatekaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediatekaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabLayoutMediator = TabLayoutMediator(binding.tabMediateka, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = resources.getString(R.string.top_tracks)
                else -> tab.text = resources.getString(R.string.playlists)
            }
        }
        tabLayoutMediator.attach()

        binding.backImage.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}