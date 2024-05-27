package com.practicum.playlistmaker.data.settings.impl

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import java.io.Serializable

class SettingsRepositoryImpl(app: Application): SettingsRepository, Serializable, Application() {


    private companion object {
        const val APP_THEME_NAME = "theme_shared_preferences"
        const val APP_THEME_SWITCHER = "theme_switch_status"
    }

    private val sharedPrefs = app.getSharedPreferences(
        APP_THEME_NAME,
        MODE_PRIVATE
    )

    private var darkTheme = getThemeSettings()

    override fun getThemeSettings(): Boolean {
        return sharedPrefs.getBoolean(APP_THEME_SWITCHER, false)
    }

    override fun updateThemeSetting(checked: Boolean) {
        sharedPrefs.edit().putBoolean(APP_THEME_SWITCHER, checked).apply()
    }

    override fun darkThemeSwitch(theme: Boolean) {
        darkTheme = theme
        AppCompatDelegate.setDefaultNightMode(
            if (theme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}