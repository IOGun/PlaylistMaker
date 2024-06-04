package com.practicum.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.settings.SettingsInteractor

class App : Application() {
   /* companion object {
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
    } */

    private lateinit var themeSettingsInteractor: SettingsInteractor

    override fun onCreate() {
        super.onCreate()
        Creator.initApplication(this)
        themeSettingsInteractor =
            Creator.provideSettingsInteractor()
        darkThemeSwitch(themeSettingsInteractor.getThemeSettings())
    }


    fun darkThemeSwitch(darkThemeEnabled: Boolean) { //switchTheme
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        themeSettingsInteractor.updateThemeSetting(darkThemeEnabled)
    }
}