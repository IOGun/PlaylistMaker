package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator

class SearchViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(
            trackInteractor = Creator.provideTrackInteractor(),
            historyInteractor = Creator.provideHistoryInteractor()
        ) as T
    }
}