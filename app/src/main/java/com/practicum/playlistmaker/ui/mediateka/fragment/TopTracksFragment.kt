package com.practicum.playlistmaker.ui.mediateka.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentTopTracksBinding
import com.practicum.playlistmaker.ui.mediateka.view_model.TopTracksViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TopTracksFragment: Fragment() {

    companion object {
        fun newInstance() = TopTracksFragment()
    }

    private var _binding: FragmentTopTracksBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<TopTracksViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTopTracksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}