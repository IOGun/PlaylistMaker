package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.Impl.HistoryRepositoryImpl
import com.practicum.playlistmaker.data.search.network.Impl.TrackRepositoryImpl
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.search.HistoryRepository
import com.practicum.playlistmaker.domain.search.TrackRepository
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    factory<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    factory<HistoryRepository> {
        HistoryRepositoryImpl(get(named("search_history")), get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get()) // ?? Look at pRI
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get(named("darkTheme")))
    }

    factory<SharingRepository> {
        SharingRepositoryImpl(androidContext())
    }

}