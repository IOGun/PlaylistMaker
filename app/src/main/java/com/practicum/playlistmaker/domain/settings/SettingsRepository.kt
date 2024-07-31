package com.practicum.playlistmaker.domain.settings

interface SettingsRepository {

    fun getThemeFromShared(): SettingsInteractor.NightLightTheme
    fun setThemeToShared(status: SettingsInteractor.NightLightTheme)
}