package com.practicum.playlistmaker.ui.settings.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Switch
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingFactory
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {


    private lateinit var binding: ActivitySettingsBinding
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SettingFactory()
        )[SettingsViewModel::class.java]
    }
    private var currentTheme: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backImage.setOnClickListener {
            finish()
        }


        binding.shareFrame.setOnClickListener {
            viewModel.shareApp()?.let { startActivity(viewModel.settingEvent.value) }
        }


        binding.supportFrame.setOnClickListener {
            viewModel.openSupport()?.let { startActivity(viewModel.settingEvent.value) }
        }



        binding.termsFrame.setOnClickListener {
            viewModel.openTerms()?.let { startActivity(viewModel.settingEvent.value) }
        }


        viewModel.darkTheme.observe(this) {isChecked ->
            binding.darkThemeSwitch.isChecked = isChecked
        }

        binding.darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            if (currentTheme != checked) {
                currentTheme = checked
                viewModel.updateThemeSetting(checked)
                binding.root.post {
                    viewModel.darkThemeSwitch(checked)
                }
            }
        }



        viewModel.darkTheme.observe(this) { isChecked ->
            binding.darkThemeSwitch.isChecked = isChecked
        }


    }
}