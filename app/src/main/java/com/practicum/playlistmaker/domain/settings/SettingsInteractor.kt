package com.practicum.playlistmaker.domain.settings

interface SettingsInteractor {

    fun applyTheme(): NightLightTheme
    fun setThemeToShared(status: NightLightTheme)

    sealed class NightLightTheme {
        object Night : NightLightTheme()
        object Light : NightLightTheme()
    }
}