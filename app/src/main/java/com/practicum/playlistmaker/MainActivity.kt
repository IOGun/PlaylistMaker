package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.findButton.setOnClickListener {
            val findIntent = Intent(this, SearchActivity::class.java)
            startActivity(findIntent)
        }

        binding.mediaButton.setOnClickListener {
            val mediatekaIntent = Intent(this, MediatekaActivity::class.java)
            startActivity(mediatekaIntent)
        }

        binding.settingsButton.setOnClickListener {
            val settIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settIntent)
        }


    }


}