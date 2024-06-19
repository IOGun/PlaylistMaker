package com.practicum.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.sharing.SharingRepository

class SharingRepositoryImpl(private val context: Context): SharingRepository {
    override fun shareApp(): Intent {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_TEXT, (context.getString(R.string.android_course_url)))
        shareIntent.setType("text/plain")
        return shareIntent
    }

    override fun openSupport(): Intent {
        val supportIntent = Intent(Intent.ACTION_SENDTO)
        supportIntent.data =
            Uri.parse("mailto:")

        supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.my_mail)))
        supportIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.title))
        supportIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.message))
        return supportIntent
    }

    override fun openTerms(): Intent {
        val termsIntent = Intent()
        termsIntent.action = Intent.ACTION_VIEW
        termsIntent.data = Uri.parse(context.getString(R.string.offer_url))
        return termsIntent
    }
}