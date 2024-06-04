package com.practicum.playlistmaker.domain.settings

interface SettingsRepository {
    fun getThemeSettings(): Boolean
    fun updateThemeSetting(checked: Boolean)
    fun darkThemeSwitch(theme: Boolean)
}