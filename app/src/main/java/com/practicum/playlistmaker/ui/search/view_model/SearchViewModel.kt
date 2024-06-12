package com.practicum.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.domain.search.HistoryInteractor
import com.practicum.playlistmaker.domain.search.TrackInteractor
import com.practicum.playlistmaker.domain.search.model.Status
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.domain.search.model.TrackSearchResult

class SearchViewModel(
    val trackInteractor: TrackInteractor,
    val historyInteractor: HistoryInteractor,
) : ViewModel(), TrackInteractor.TrackConsumer {

    companion object {
        private const val EMPTY = ""
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }

    private val privFoundTracks: MutableLiveData<TrackSearchResult> =
        MutableLiveData(TrackSearchResult(tracks = emptyList(), Status.DEFAULT))

    val foundTracks: LiveData<TrackSearchResult> get() = privFoundTracks

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        privFoundTracks.postValue(getStatus())
        performSearch()
    }

    var textSearch: String = EMPTY

    private fun performSearch() {
        if (textSearch.isNotEmpty()) {
            trackInteractor.searchTrack(
                textSearch, this
            )
        }
    }

    override fun consume(foundTracks: TrackSearchResult) {
        when (foundTracks.status) {
            Status.RECEIVED -> {
                this.privFoundTracks.postValue(foundTracks)
            }

            Status.NOT_FOUND, Status.ERROR, Status.DEFAULT -> {
                this.privFoundTracks.postValue(foundTracks)
            }

            Status.LOADING -> {

            }
        }
    }

    fun removeCallbacks() {
        handler.removeCallbacks(searchRunnable)
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
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY_MILLIS)
    }

    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
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