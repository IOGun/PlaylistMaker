package com.practicum.playlistmaker.domain.settings

interface SettingsInteractor {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(checked: Boolean)
    fun darkThemeSwitch(theme: Boolean)
}