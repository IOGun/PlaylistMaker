package com.practicum.playlistmaker.data.settings.impl

import android.app.Application
import android.content.SharedPreferences
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import java.io.Serializable

class SettingsRepositoryImpl(private val sharedPrefs: SharedPreferences): SettingsRepository, Serializable, Application() {


    private companion object {
        const val APP_THEME_SWITCHER = "theme_switch_status"
    }


    override fun getThemeFromShared(): SettingsInteractor.NightLightTheme {
        return when (sharedPrefs.getBoolean(APP_THEME_SWITCHER, false)) {
            true -> SettingsInteractor.NightLightTheme.Night
            false -> SettingsInteractor.NightLightTheme.Light
        }
    }


    override fun setThemeToShared(status: SettingsInteractor.NightLightTheme) {

        sharedPrefs.edit().putBoolean(
            APP_THEME_SWITCHER,
            when (status) {
                SettingsInteractor.NightLightTheme.Light -> false
                SettingsInteractor.NightLightTheme.Night -> true
            }
        ).apply()
    }

}