package com.practicum.playlistmaker.domain.sharing

import android.content.Intent

interface SharingRepository {
    fun shareApp(): Intent
    fun openSupport(): Intent
    fun openTerms(): Intent
}