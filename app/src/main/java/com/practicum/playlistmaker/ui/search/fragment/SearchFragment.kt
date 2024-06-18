package com.practicum.playlistmaker.ui.search.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.FragmentFindBinding
import com.practicum.playlistmaker.domain.search.model.Status
import com.practicum.playlistmaker.domain.search.model.Track
import com.practicum.playlistmaker.domain.search.model.TrackSearchResult
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity
import com.practicum.playlistmaker.ui.search.adapters.SearchHistoryAdapter
import com.practicum.playlistmaker.ui.search.adapters.TrackAdapter
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        private const val SEARCH_TEXT_KEY = "TEXT_KEY"
        private const val EMPTY = ""
        private const val TRACK_KEY = "track"
    }

    private lateinit var binding: FragmentFindBinding

    private val viewModel by viewModel<SearchViewModel>()

    private val searchAdapter = TrackAdapter()
    private val historyAdapter = SearchHistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFindBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.foundTracks.observe(viewLifecycleOwner) { it ->
            performSearching(it)
        }

        toSetupAdapters()

        toSetupEditText()

        toSetupButtons()

        loadHistory()

        toSetupClickListener()

        restoreInstanceState(savedInstanceState)
    }

    private fun toSetupAdapters() {

        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = searchAdapter

        binding.searchHistoryRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.searchHistoryRecyclerView.adapter = historyAdapter
    }

    private fun toSetupEditText() {

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
                binding.clearButton.isVisible = !s.isNullOrEmpty()

                binding.searchHistory.visibility =
                    if (binding.findEditText.hasFocus() && s?.isEmpty() == true && viewModel.loadHistory()
                            .isNotEmpty()
                    ) View.VISIBLE
                    else View.GONE

                if (binding.findEditText.text.toString().isEmpty()) {
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
    }

    private fun toSetupButtons() {

        binding.clearButton.setOnClickListener {
            clearSearch()
        }


        binding.searchHistoryClearButton.setOnClickListener {
            historyAdapter.tracks.clear()
            viewModel.clearSearchHistory()
            historyAdapter.notifyDataSetChanged()
            binding.searchHistory.visibility = View.GONE
        }

        binding.updateButton.setOnClickListener {
            if (viewModel.textSearch.isNotEmpty()) {
                binding.findEditText.setText(viewModel.textSearch)
                viewModel.searchDebounce()
            }
        }
    }

    private fun toSetupClickListener() {

        searchAdapter.itemClickListener = object : TrackAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                openPlayerActiviti(track)
            }
        }


        historyAdapter.itemClickListener = object : SearchHistoryAdapter.OnItemClickListener {
            override fun onItemClick(track: Track) {
                openPlayerActiviti(track)
            }
        }
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            viewModel.textSearch = savedInstanceState.getString(
                SEARCH_TEXT_KEY,
                EMPTY
            ) ?: EMPTY
            binding.findEditText.setText(viewModel.textSearch)
            if (viewModel.textSearch.isNotEmpty()) {
                viewModel.searchDebounce()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeCallbacks()
    }

    override fun onResume() {
        super.onResume()
        loadHistory()
        historyAdapter.notifyDataSetChanged()

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.textSearch =
            binding.findEditText.text.toString()
        outState.putString(
            SEARCH_TEXT_KEY,
            viewModel.textSearch
        )
    }

    private fun openPlayerActiviti(track: Track) {
        if (viewModel.clickDebounce()) {
            val json = Gson()
            val data = json.toJson(track)
            val playerIntent = Intent(requireContext(), PlayerActivity::class.java)

            playerIntent.putExtra(TRACK_KEY, data)

            playerIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(playerIntent)

            viewModel.addToHistory(track)
        }
    }

    private fun clearSearch() {
        binding.findEditText.setText(EMPTY)
        closeKeyboard()
        searchAdapter.tracks.clear()
        searchAdapter.notifyDataSetChanged()
        closePlaceholders()
    }

    private fun loadHistory() {
        val history = viewModel.loadHistory()
        historyAdapter.tracks.clear()
        historyAdapter.tracks.addAll(history)
        historyAdapter.notifyDataSetChanged()
    }

    private fun closeKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(binding.findEditText.windowToken, 0)
    }

    private fun closePlaceholders() {
        binding.notFoundPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.GONE
    }

    private fun nothingFoundPlaceholder() {
        binding.searchHistory.visibility = View.GONE
        binding.notFoundPlaceholder.visibility = View.VISIBLE
        binding.errorPlaceholder.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
    }

    private fun errorPlaceholder() {
        binding.notFoundPlaceholder.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.VISIBLE
        binding.updateButton.visibility = View.VISIBLE
    }

    private fun performSearching(foundTracks: TrackSearchResult) {
        searchAdapter.tracks.clear()
        binding.searchHistory.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        when (foundTracks.status) {
            Status.RECEIVED -> {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                searchAdapter.tracks.addAll(foundTracks.tracks)
                searchAdapter.notifyDataSetChanged()
            }

            Status.NOT_FOUND -> {
                binding.progressBar.visibility = View.GONE
                nothingFoundPlaceholder()
            }

            Status.ERROR -> {
                binding.progressBar.visibility = View.GONE
                errorPlaceholder()
            }

            Status.DEFAULT -> {
                binding.progressBar.visibility = View.GONE
            }

            Status.LOADING -> {
                binding.progressBar.visibility = View.VISIBLE
            }

        }
    }

}