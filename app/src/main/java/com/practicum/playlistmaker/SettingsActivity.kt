package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import java.net.URI

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back_image)
        backButton.setOnClickListener {
            finish()
        }

        val shareButton = findViewById<ImageView>(R.id.share_image)
        shareButton.setOnClickListener {
            finish()
        }

        val supportButton = findViewById<ImageView>(R.id.support_image)
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

        val termsButton = findViewById<ImageView>(R.id.terms_image)
        termsButton.setOnClickListener {
            val srcOffert = getString(R.string.src_practicum_offer)
            val termsIntent = Intent()
            termsIntent.action = Intent.ACTION_VIEW
            termsIntent.data = Uri.parse(srcOffert)
            startActivity(termsIntent)
        }

        val darkThemeSwitch = findViewById<Switch>(R.id.dark_theme_switch)
        darkThemeSwitch.setOnClickListener {
            //AppCompatDelegate.MODE_NIGHT_YES
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            //saveTheme(THEME_DARK)
        }
    }
}