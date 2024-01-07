package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    companion object {
        const val SEARCH_TEXT_KEY = "TEXT_KEY"
        const val EMPTY = ""
        const val SEARCH_HISTORY_PREFERENCES = "search_history_preferences"
        const val MAX_HISTORY_SIZE = 10
    }

    private var valueFromET = EMPTY
    private var searchText = EMPTY
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT_KEY, valueFromET)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        valueFromET = savedInstanceState.getString(SEARCH_TEXT_KEY, EMPTY)

    }


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)


        val clearButton = findViewById<ImageView>(R.id.clearButton)
        val editText = findViewById<EditText>(R.id.findEditText)
        val backButton = findViewById<ImageView>(R.id.backImage)
        val notFoundPlaceholder = findViewById<LinearLayout>(R.id.notFoundPlaceholder)
        val errorPlaceholder = findViewById<LinearLayout>(R.id.errorPlaceholder)
        val updateButton = findViewById<Button>(R.id.updateButton)
        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val searchHistoryPlaceholder = findViewById<LinearLayout>(R.id.searchHistory)
        val searchHistoryRecyclerView = findViewById<RecyclerView>(R.id.searchHistoryRecyclerView)
        val searchHistoryClearButton = findViewById<Button>(R.id.searchHistoryClearButton)

        editText.setText(valueFromET)

        recyclerView.layoutManager = LinearLayoutManager(this)
        searchHistoryRecyclerView.layoutManager = LinearLayoutManager(this)

        // val playlist = MockObjects.getPlaylist()
        val tracks: MutableList<Track> = mutableListOf()

        val trackAdapter = TrackAdapter()
        trackAdapter.tracks = tracks
        recyclerView.adapter = trackAdapter


        val sharedPreferences = getSharedPreferences(SEARCH_HISTORY_PREFERENCES, MODE_PRIVATE)
        val searchHistory = SearchHistory(sharedPreferences)
        val history = searchHistory.read()

        trackAdapter.itemClickListener = TrackAdapter.ItemClickListener {

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

            Log.d("CLICK", "TRACK ${it.trackName} ID ${it.trackId}  ")
        }

        val searchHistoryAdapter = SearchHistoryAdapter()

        clearButton.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            editText.setText(EMPTY)
            recyclerView.visibility = View.GONE
            notFoundPlaceholder.visibility = View.GONE
            errorPlaceholder.visibility = View.GONE
        }

        editText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && editText.text.isEmpty()) {
                val tracks = searchHistory.read()
                if (tracks.isNotEmpty()) {
                    searchHistoryPlaceholder.visibility = View.VISIBLE
                    searchHistoryAdapter.tracks = tracks
                    searchHistoryRecyclerView.adapter = searchHistoryAdapter
                } else {
                    searchHistoryPlaceholder.visibility = View.GONE
                }

                searchHistoryClearButton.setOnClickListener {
                    searchHistory.clear()
                    searchHistoryPlaceholder.visibility = View.GONE
                }

            } else {
                searchHistoryPlaceholder.visibility = View.GONE
            }
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
                if (p0.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                    valueFromET = p0.toString()
                }
            }

            override fun afterTextChanged(p0: Editable?): Unit {}
        }

        editText.addTextChangedListener(watcher)

        fun iTunesServiceSearch(text: String) {
            iTunesService
                .search(text)
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            tracks.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                tracks.addAll(response.body()?.results!!)
                                Log.d("RES", "${response.body()?.results!!}")
                                trackAdapter.notifyDataSetChanged()
                            }
                            if (tracks.isEmpty()) {
                                //nothing found
                                recyclerView.visibility = View.GONE
                                errorPlaceholder.visibility = View.GONE
                                notFoundPlaceholder.visibility = View.VISIBLE
                            } else {
                                // found
                                notFoundPlaceholder.visibility = View.GONE
                                errorPlaceholder.visibility = View.GONE
                                recyclerView.visibility = View.VISIBLE
                            }
                        } else {
                            // something wrong
                            recyclerView.visibility = View.GONE
                            notFoundPlaceholder.visibility = View.GONE
                            errorPlaceholder.visibility = View.VISIBLE
                        }
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        recyclerView.visibility = View.GONE
                        notFoundPlaceholder.visibility = View.GONE
                        errorPlaceholder.visibility = View.VISIBLE
                    }
                })
        }

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText.text.isNotEmpty()) {
                    searchText = editText.text.toString()
                    iTunesServiceSearch(searchText)

                }
                true
            }
            false
        }

        updateButton.setOnClickListener {
            iTunesServiceSearch(searchText)
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}