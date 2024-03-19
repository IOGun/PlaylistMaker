package com.practicum.playlistmaker.presentation.settings

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    companion object{
        private const val DAY_NIGHT_PREFERENCE = "day_night_theme"
        private const val DAY_NIGHT_KEY = "day_night"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.backImage)
        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<FrameLayout>(R.id.shareFrame)
        shareButton.setOnClickListener {
            val message = getString(R.string.android_course_url)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.setType("text/plain")
            startActivity(shareIntent)
        }

        val supportButton = findViewById<FrameLayout>(R.id.supportFrame)
        supportButton.setOnClickListener {
            val message = getString(R.string.message)
            val title = getString(R.string.title)
            val myMail = getString(R.string.my_mail)
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data =
                Uri.parse("mailto:")

            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(myMail))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, title)
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(supportIntent)
        }

        val termsButton = findViewById<FrameLayout>(R.id.termsFrame)
        termsButton.setOnClickListener {
            val srcOffert = getString(R.string.offer_url)
            val termsIntent = Intent()
            termsIntent.action = Intent.ACTION_VIEW
            termsIntent.data = Uri.parse(srcOffert)
            startActivity(termsIntent)
        }

        val sharedPrefs = getSharedPreferences(DAY_NIGHT_PREFERENCE, MODE_PRIVATE) //darkTheme from app

        val darkThemeSwitch = findViewById<Switch>(R.id.darkThemeSwitch)

        darkThemeSwitch.isChecked = sharedPrefs.getBoolean(DAY_NIGHT_KEY, false)

        darkThemeSwitch.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switcnTheme(checked)
            sharedPrefs.edit().putBoolean(DAY_NIGHT_KEY, checked).apply()
        }
    }
}