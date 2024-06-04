package com.practicum.playlistmaker.domain.sharing.impl

import android.content.Intent
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.SharingRepository

class SharingInteractorImpl(
    private val repository: SharingRepository,
) : SharingInteractor {
    override fun shareApp(): Intent {
        return repository.shareApp()
    }

    override fun openTerms(): Intent {
        return repository.openTerms()
    }

    override fun openSupport(): Intent {
       return repository.openSupport()
    }

}