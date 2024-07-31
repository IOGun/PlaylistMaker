package com.practicum.playlistmaker.domain.settings.impl

import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {


    override fun applyTheme(): SettingsInteractor.NightLightTheme {
        val theme = settingsRepository.getThemeFromShared()
        setTheme(theme)
        return theme
    }

    override fun setThemeToShared(status: SettingsInteractor.NightLightTheme) {

        settingsRepository.setThemeToShared(status)
    }

    private fun setTheme(status: SettingsInteractor.NightLightTheme) {
        AppCompatDelegate.setDefaultNightMode(
            when (status) {
                SettingsInteractor.NightLightTheme.Light -> AppCompatDelegate.MODE_NIGHT_NO
                SettingsInteractor.NightLightTheme.Night -> AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }
}