package com.practicum.playlistmaker

import android.app.Application
import android.content.res.Resources.Theme
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences("day_night_theme", MODE_PRIVATE)
        switcnTheme(sharedPrefs.getBoolean("day_night", false))

    }

    fun switcnTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}