package com.practicum.playlistmaker.presentation.search

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.Creator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.network.SearchHistory
import com.practicum.playlistmaker.databinding.ActivityFindBinding
import com.practicum.playlistmaker.domain.api.TrackInteractor
import com.practicum.playlistmaker.domain.models.AppCollectionStatus
import com.practicum.playlistmaker.domain.models.Track
import com.practicum.playlistmaker.domain.models.TrackSearchResult
import com.practicum.playlistmaker.presentation.player.PlayerActivity


class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_TEXT_KEY = "TEXT_KEY"
        private const val EMPTY = ""
        private const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"
        private const val MAX_HISTORY_SIZE = 10
        private const val TRACK_KEY = "track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var valueFromET = EMPTY
    private var searchText = EMPTY

    private lateinit var binding: ActivityFindBinding
    private lateinit var trackInteractor: TrackInteractor

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, valueFromET)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        valueFromET = savedInstanceState.getString(SEARCH_TEXT_KEY, EMPTY)

    }


    /* private val retrofit = Retrofit.Builder()
         .baseUrl("https://itunes.apple.com")
         .addConverterFactory(GsonConverterFactory.create())
         .build()

     private val iTunesService = retrofit.create(ITunesApi::class.java)*/

    private var isClickAllowed = true
    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var searchHistory: SearchHistory

    private val tracks: MutableList<Track> = mutableListOf()

    private var history: MutableList<Track> = mutableListOf()
    private val trackAdapter = TrackAdapter {
        if (clickDebounce()) {
            var i = 0
            for (track in history) {
                if (track.trackId == it.trackId) {
                    history.removeAt(i)
                    i = 0
                    break
                }
                i++
            }
            history.add(0, it)
            if (history.size > MAX_HISTORY_SIZE) {
                history.removeAt(history.size - 1)
            }
            searchHistory.write(history)
            openPlayerActiviti(it)
        }
    }
    private val searchHistoryAdapter = SearchHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        binding = ActivityFindBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackInteractor = Creator.provideTrackInteractor()

        binding.findEditText.setText(valueFromET)

        trackAdapter.tracks = tracks
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = trackAdapter

        sharedPreferences = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)
        history = searchHistory.read()
        binding.searchHistoryRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter //


        searchHistoryAdapter.itemClickListener = TrackAdapter.ItemClickListener {
            if (clickDebounce()) {
                openPlayerActiviti(it)
            }
        }


        binding.clearButton.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.findEditText.windowToken, 0)
            binding.findEditText.setText(EMPTY)
            binding.recyclerView.isVisible = false
            binding.notFoundPlaceholder.isVisible = false
            binding.errorPlaceholder.isVisible = false
        }

        binding.findEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.findEditText.text.isEmpty()) {
                val tracksHistory = searchHistory.read()
                if (tracksHistory.isNotEmpty()) {
                    binding.searchHistory.isVisible = true
                    searchHistoryAdapter.tracks = tracksHistory
                    binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter
                } else {
                    binding.searchHistory.isVisible = false
                }


            } else {
                binding.searchHistory.isVisible = false
            }
        }

        binding.searchHistoryClearButton.setOnClickListener {
            searchHistory.clear()
            binding.searchHistory.isVisible = false
        }

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ): Unit {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchText = p0.toString()
                searchDebounce()
                if (p0.isNullOrEmpty() && binding.findEditText?.hasFocus() == true) {
                    binding.clearButton.visibility = View.GONE
                    if (history.isNotEmpty()) {
                        binding.searchHistory.isVisible = true
                        searchHistoryAdapter.tracks = history
                        binding.searchHistoryRecyclerView.adapter = searchHistoryAdapter
                    } else {
                        binding.searchHistory.isVisible = false
                    }
                } else {
                    binding.clearButton.isVisible = true
                    binding.searchHistory.isVisible = false
                    valueFromET = p0.toString()
                }

            }

            override fun afterTextChanged(p0: Editable?): Unit {}
        }

        binding.findEditText.addTextChangedListener(watcher)

        binding.findEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.findEditText.text.isNotEmpty()) {
                    searchText = binding.findEditText.text.toString()
                    searchRequest()
                }
                true
            }
            false
        }

        binding.updateButton.setOnClickListener { searchRequest() }

        binding.backImage.setOnClickListener { finish() }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(searchRunnable)
    }


    private fun openPlayerActiviti(track: Track) {
        val json = Gson()
        val data = json.toJson(track)
        val playerIntent = Intent(this, PlayerActivity::class.java)
        playerIntent.putExtra(TRACK_KEY, data)
        startActivity(playerIntent)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun searchRequest() {
        if (searchText.isNotEmpty()) {
            binding.notFoundPlaceholder.isVisible = false
            binding.errorPlaceholder.isVisible = false
            binding.recyclerView.isVisible = false
            binding.searchProgressBar.isVisible = true


            trackInteractor.searchTrack(
                searchText, consumer =
                object : TrackInteractor.TrackConsumer {
                    override fun consume(foundTrack: TrackSearchResult) {
                        handler.post {
                            binding.searchProgressBar.isVisible = false
                            when (foundTrack.status) {
                                AppCollectionStatus.RECEIVED -> {
                                    // found
                                    if (foundTrack.tracks.isNotEmpty()) {
                                        tracks.clear()
                                        tracks.addAll(foundTrack.tracks)
                                        trackAdapter.notifyDataSetChanged()
                                        binding.recyclerView.isVisible = true
                                    } else {
                                        //nothing found
                                        binding.recyclerView.isVisible = false
                                        binding.errorPlaceholder.isVisible = false
                                        binding.notFoundPlaceholder.isVisible = true
                                    }
                                }
                                AppCollectionStatus.ERROR -> {
                                    // something wrong
                                    binding.searchProgressBar.isVisible = false
                                    binding.recyclerView.isVisible = false
                                    binding.notFoundPlaceholder.isVisible = false
                                    binding.errorPlaceholder.isVisible = true
                                }
                                else -> {
                                    // something wrong
                                    binding.searchProgressBar.isVisible = false
                                    binding.recyclerView.isVisible = false
                                    binding.notFoundPlaceholder.isVisible = false
                                    binding.errorPlaceholder.isVisible = true
                                }
                            }
                        }
                    }
                }
            )
        }
    }

}