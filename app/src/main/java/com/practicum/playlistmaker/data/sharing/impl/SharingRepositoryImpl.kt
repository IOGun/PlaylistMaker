package com.practicum.playlistmaker.data.sharing.impl

import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.sharing.SharingRepository

class SharingRepositoryImpl(private val app: App): SharingRepository {

    override fun shareApp(): Intent {
        val message = app.getString(R.string.android_course_url)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        shareIntent.setType("text/plain")
        return shareIntent
    }

    override fun openSupport(): Intent {
        val message = app.getString(R.string.message)
        val title = app.getString(R.string.title)
        val myMail = app.getString(R.string.my_mail)
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data =
            Uri.parse("mailto:")

        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(myMail))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        supportIntent.putExtra(Intent.EXTRA_TEXT, message)
        return supportIntent
    }

    override fun openTerms(): Intent {
        val srcOffert = app.getString(R.string.offer_url)
        val termsIntent = Intent()
        termsIntent.action = Intent.ACTION_VIEW
        termsIntent.data = Uri.parse(srcOffert)
        return termsIntent
    }
}