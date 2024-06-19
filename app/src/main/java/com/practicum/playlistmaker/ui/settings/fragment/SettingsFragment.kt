package com.practicum.playlistmaker.ui.settings.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.isThemeSwitcherEnabled.observe(viewLifecycleOwner) { isChecked ->
            binding.darkThemeSwitch.isChecked = isChecked
        }

        viewModel.settingsIntentEvent.observe(viewLifecycleOwner) { activity ->
            activity.wasntOpen()?.let { intent ->
                startActivity(intent)
            }
        }


        binding.darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            viewModel.onThemeSwitcherChecked(checked)
        }


        binding.shareFrame.setOnClickListener {
            viewModel.onShareClick()
        }


        binding.supportFrame.setOnClickListener {
            viewModel.onSupportClick()
        }


        binding.termsFrame.setOnClickListener {
            viewModel.onTermsClick()
        }
    }
}