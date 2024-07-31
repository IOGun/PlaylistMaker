package com.practicum.playlistmaker.ui.search.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.search.HistoryInteractor
import com.practicum.playlistmaker.domain.search.TrackInteractor
import com.practicum.playlistmaker.domain.search.model.Status
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.domain.search.model.TrackSearchResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    val trackInteractor: TrackInteractor,
    val historyInteractor: HistoryInteractor,
) : ViewModel() {

    companion object {
        private const val EMPTY = ""
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }

    private val privFoundTracks: MutableLiveData<TrackSearchResult> =
        MutableLiveData(TrackSearchResult(tracks = emptyList(), Status.DEFAULT))

    val foundTracks: LiveData<TrackSearchResult> get() = privFoundTracks

    private var isClickAllowed = true

    private var job: Job? = null

    var textSearch: String = EMPTY

    private suspend fun performSearch() {
        if (textSearch.isNotEmpty()) {
            trackInteractor.searchTrack(textSearch).collect() { result ->
                privFoundTracks.postValue(result)
            }
        }
    }


    fun loadHistory(): ArrayList<Track> =
        historyInteractor.loadHistory()

    fun clearSearchHistory() {
        historyInteractor.clearSearchHistory()
    }

    fun addToHistory(track: Track) {
        historyInteractor.addToHistory(track)
        loadHistory()
    }


    fun searchDebounce() {
        job?.cancel()
        job = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            if (textSearch.length >= 2) {
                privFoundTracks.postValue(getStatus())
                performSearch()
            }
        }
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewModelScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    fun changeTextSearch(text: String) {
        textSearch = text
    }

    private fun getStatus(): TrackSearchResult {
        return TrackSearchResult(
            tracks = emptyList(),
            Status.LOADING
        )
    }

}