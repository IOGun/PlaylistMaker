package com.practicum.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonFind = findViewById<Button>(R.id.button_find)
        /*val buttonFindClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Кнопка Поиск", Toast.LENGTH_SHORT).show()
            }
        }*/
        buttonFind.setOnClickListener {
            val findIntent = Intent(this, Mediateka::class.java)
            startActivity(findIntent)
        }

        val buttonMediateka = findViewById<Button>(R.id.button_media)
        buttonMediateka.setOnClickListener {
            //Toast.makeText(this@MainActivity, "Кнопка Медиатека", Toast.LENGTH_SHORT).show()
            val mediatekaIntent = Intent(this, Mediateka::class.java)
            startActivity(mediatekaIntent)
        }

        val buttonSett = findViewById<Button>(R.id.button_sett)
        buttonSett.setOnClickListener {
            //Toast.makeText(this@MainActivity, "Кнопка Hастройки", Toast.LENGTH_SHORT).show()
            val settIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settIntent)
        }

    }


}