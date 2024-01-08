package com.practicum.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        editText.setText(valueFromET)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val playlist = MockObjects.getPlaylist()
        val tracks = ArrayList<Track>()

        val trackAdapter = TrackAdapter()
        trackAdapter.tracks = tracks
        recyclerView.adapter = trackAdapter


        clearButton.setOnClickListener {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
            editText.setText(EMPTY)
            recyclerView.visibility = View.GONE
            notFoundPlaceholder.visibility = View.GONE
            errorPlaceholder.visibility = View.GONE
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