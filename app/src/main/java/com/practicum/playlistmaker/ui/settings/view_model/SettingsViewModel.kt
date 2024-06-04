package com.practicum.playlistmaker.ui.settings.view_model

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel (
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val privSettingEvent = MutableLiveData<Intent>()
    val settingEvent: LiveData<Intent> = privSettingEvent

    private val getDarkTheme = MutableLiveData(getThemeSettings())
    val darkTheme: LiveData<Boolean> = getDarkTheme

    fun shareApp() {
        privSettingEvent.value = sharingInteractor.shareApp()
    }

    fun openSupport() {
        privSettingEvent.value = sharingInteractor.openSupport()
    }

    fun openTerms() {
        privSettingEvent.value = sharingInteractor.openTerms()
    }

    private fun getThemeSettings(): Boolean {
        return settingsInteractor.getThemeSettings()
    }
    fun updateThemeSetting(checked: Boolean) {
        settingsInteractor.updateThemeSetting(checked)
    }
    fun darkThemeSwitch(theme: Boolean) {
        settingsInteractor.darkThemeSwitch(theme)
    }

}