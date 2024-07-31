package com.practicum.playlistmaker.ui.settings.utilits

class WasOpened<out Intent>(private val intent: Intent) {
    private var flag = false
    fun wasntOpen(): Intent? {
        return if (flag) {
            null
        } else {
            flag = true
            intent
        }
    }
}