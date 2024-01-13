package com.practicum.playlistmaker

import android.app.Application
import android.content.res.Resources.Theme
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    companion object {
        private const val DAY_NIGHT_PREFERENCE = "day_night_theme"
        private const val DAY_NIGHT_KEY = "day_night"
    }

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(DAY_NIGHT_PREFERENCE, MODE_PRIVATE)
        switcnTheme(sharedPrefs.getBoolean(DAY_NIGHT_KEY, false))

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