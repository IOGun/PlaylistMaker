package com.practicum.playlistmaker.ui.search.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityFindBinding
import com.practicum.playlistmaker.domain.search.model.Status
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.domain.search.model.TrackSearchResult
import com.practicum.playlistmaker.ui.search.adapters.SearchHistoryAdapter
import com.practicum.playlistmaker.ui.search.adapters.TrackAdapter
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {
    companion object {
        private const val SEARCH_TEXT_KEY = "TEXT_KEY"
        private const val EMPTY = ""
        private const val TRACK_KEY = "track"
    }

    private lateinit var binding: ActivityFindBinding
/*
    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this, SearchViewModelFactory())[SearchViewModel::class.java]
    } */

    private val viewModel by viewModel<SearchViewModel>()


    private lateinit var searchTracks: ArrayList<Track>
    private lateinit var historyTracks: ArrayList<Track>
    private var searchText = EMPTY

    private lateinit var searchAdapter: TrackAdapter
    private lateinit var historyAdapter: SearchHistoryAdapter


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.textSearch = binding.findEditText.text.toString()
        outState.putString(SEARCH_TEXT_KEY, viewModel.textSearch)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        binding = ActivityFindBinding.inflate(layoutInflater)
        setContentView(binding.root)


        viewModel.foundTracks.observe(this) { it ->
            performSearching(it)
        }

        searchTracks = ArrayList<Track>()
        historyTracks = ArrayList<Track>()

        searchAdapter = TrackAdapter()
        historyAdapter = SearchHistoryAdapter()


        searchAdapter.tracks = searchTracks
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = searchAdapter

        historyAdapter.tracks = historyTracks
        binding.searchHistoryRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.searchHistoryRecyclerView.adapter = historyAdapter


        binding.findEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce()
                true
            } else {
                false
            }
        }

        binding.findEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.findEditText.text.isEmpty() && viewModel.loadHistory()
                    .isNotEmpty()
            ) {
                binding.searchHistory.visibility = View.VISIBLE
                viewModel.loadHistory()
                historyAdapter.notifyDataSetChanged()
            } else if (viewModel.loadHistory().isNotEmpty()) {
                binding.searchHistory.visibility = View.VISIBLE
            } else {
                binding.searchHistory.visibility = View.GONE
            }
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.textSearch = s.toString()
                binding.clearButton.visibility = buttonVisibility(s)
                binding.searchHistory.visibility =
                    if (binding.findEditText.hasFocus() && s?.isEmpty() == true && viewModel.loadHistory()
                            .isNotEmpty()
                    ) View.VISIBLE
                    else View.GONE
                searchText = binding.findEditText.text.toString()
                if (searchText.isEmpty()) {
                    closePlaceholders()
                    viewModel.removeCallbacks()
                } else {
                    viewModel.changeTextSearch(binding.findEditText.text.toString())
                    viewModel.searchDebounce()
                    closePlaceholders()
                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        binding.findEditText.addTextChangedListener(simpleTextWatcher)


        binding.clearButton.setOnClickListener {
            clearSearch()
        }

        binding.searchHistoryClearButton.setOnClickListener {
            historyTracks.clear()
            viewModel.clearSearchHistory()
            historyAdapter.notifyDataSetChanged()
            binding.searchHistoryRecyclerView.visibility = View.GONE
        }

        binding.updateButton.setOnClickListener {
            if (viewModel.textSearch.isNotEmpty()) {
                binding.findEditText.setText(viewModel.textSearch)
                viewModel.searchDebounce()
            }
        }

        binding.backImage.setOnClickListener {
            finish()
        }


        loadHistory()


        searchAdapter.setItemClickListener(object : TrackAdapter.ItemClickListener {
            override fun onClick(track: Track) {
                openPlayerActiviti(track)
            }
        })
        historyAdapter.setItemClickListener(object : SearchHistoryAdapter.ItemClickListener {
            override fun onClick(track: Track) {

                openPlayerActiviti(track)
            }
        })

        restoreInstanceState(savedInstanceState)

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeCallbacks()
    }

    override fun onResume() {
        super.onResume()
        loadHistory() //!
        historyAdapter.notifyDataSetChanged()
    }



    private fun loadHistory() {
        val history = viewModel.loadHistory()
        historyTracks.clear()
        historyTracks.addAll(history)
        historyAdapter.notifyDataSetChanged()
    }


    private fun restoreInstanceState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            viewModel.textSearch = savedInstanceState.getString(
                SEARCH_TEXT_KEY,
                EMPTY
            )
            binding.findEditText.setText(viewModel.textSearch)
            if (viewModel.textSearch.isNotEmpty()) {
                viewModel.searchDebounce()
            }
        }
    }

    private fun buttonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
    private fun performSearching(foundTracks: TrackSearchResult) {
        searchTracks.clear()
        binding.recyclerView.visibility = View.GONE
        binding.searchProgressBar.visibility = View.GONE

        when (foundTracks.status) {
            Status.RECEIVED -> {
                binding.searchProgressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                searchTracks.addAll(foundTracks.tracks)
                searchAdapter.notifyDataSetChanged()
            }

            Status.NOT_FOUND -> {
                binding.searchProgressBar.visibility = View.GONE
                nothingFoundPlaceholder()
            }

            Status.ERROR -> {
                binding.searchProgressBar.visibility = View.GONE
                errorPlaceholder()
            }

            Status.DEFAULT -> {
                binding.searchProgressBar.visibility = View.GONE
            }

            Status.LOADING -> {
                binding.searchProgressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun clearSearch() {
        binding.findEditText.setText(EMPTY)
        closeKeyboard()
        searchTracks.clear()
        searchAdapter.notifyDataSetChanged()
        closePlaceholders()
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.findEditText.windowToken, 0)
    }

    private fun closePlaceholders() {
        binding.notFoundPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.GONE
    }

    private fun nothingFoundPlaceholder() {
        binding.searchHistoryRecyclerView.visibility = View.GONE
        binding.notFoundPlaceholder.visibility = View.VISIBLE
        binding.errorPlaceholder.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
    }

    private fun errorPlaceholder() {
        binding.notFoundPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.VISIBLE
        binding.updateButton.visibility = View.VISIBLE
    }

    private fun openPlayerActiviti(track: Track) {
        if (viewModel.clickDebounce()) {
            val json = Gson()
            val data = json.toJson(track)
            val playerIntent = Intent(this, PlayerActivity::class.java)

            playerIntent.putExtra(TRACK_KEY, data)

            playerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(playerIntent)

            viewModel.addToHistory(track)
        }
    }


}