package com.practicum.playlistmaker.domain.settings.impl

import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SettingsRepository

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {
    override fun getThemeSettings(): Boolean {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(checked: Boolean) {
        return settingsRepository.updateThemeSetting(checked)
    }

    override fun darkThemeSwitch(theme: Boolean) {
        return settingsRepository.darkThemeSwitch(theme)
    }
}