package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonFind = findViewById<Button>(R.id.findButton)
        buttonFind.setOnClickListener {
            val findIntent = Intent(this, SearchActivity::class.java)
            startActivity(findIntent)
        }

        val buttonMediateka = findViewById<Button>(R.id.mediaButton)
        buttonMediateka.setOnClickListener {
            val mediatekaIntent = Intent(this, MediatekaActivity::class.java)
            startActivity(mediatekaIntent)
        }

        val buttonSett = findViewById<Button>(R.id.settingsButton)
        buttonSett.setOnClickListener {
            val settIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settIntent)
        }

    }


}