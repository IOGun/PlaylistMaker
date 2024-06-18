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

    private val _isThemeSwitcherEnabled = MutableLiveData(false)
    val isThemeSwitcherEnabled: LiveData<Boolean> = _isThemeSwitcherEnabled


    private val _settingsIntentEvent = MutableLiveData<Intent>()
    val settingsIntentEvent: LiveData<Intent> = _settingsIntentEvent


    fun onShareClick() {
        _settingsIntentEvent.value = sharingInteractor.shareApp()
    }

    fun onSupportClick() {
        _settingsIntentEvent.value = sharingInteractor.openSupport()
    }

    fun onTermsClick() {
        _settingsIntentEvent.value = sharingInteractor.openTerms()
    }

    fun setTheme(status: SettingsInteractor.NightLightTheme) {
        settingsInteractor.setThemeToShared(status)
        getTheme()
    }

    private fun getTheme(): SettingsInteractor.NightLightTheme {
        val theme = settingsInteractor.applyTheme()
        _isThemeSwitcherEnabled.value = when (theme) {
            SettingsInteractor.NightLightTheme.Light -> false
            SettingsInteractor.NightLightTheme.Night -> true
        }
        return theme
    }

    fun onThemeSwitcherChecked(checked: Boolean) {
        if (checked) {
            setTheme(SettingsInteractor.NightLightTheme.Night)
        } else {
            setTheme(SettingsInteractor.NightLightTheme.Light)
        }
    }

}