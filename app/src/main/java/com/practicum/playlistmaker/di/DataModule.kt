package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.search.network.ITunesApi
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single(named("search_history")) {
        androidContext()
            .getSharedPreferences("search_history", Context.MODE_PRIVATE)
    }

    single(named("darkTheme")) {
        androidContext()
            .getSharedPreferences("theme_shared_preferences", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    factory { MediaPlayer() }

    factory<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single(named("context")) {
        App()
    }


}