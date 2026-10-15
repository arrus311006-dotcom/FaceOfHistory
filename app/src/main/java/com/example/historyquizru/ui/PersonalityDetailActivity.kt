package com.example.historyquizru.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.historyquizru.R
import com.example.historyquizru.data.AppDatabase
import com.example.historyquizru.databinding.ActivityPersonalityDetailBinding
import com.example.historyquizru.util.ImageLoader
import kotlinx.coroutines.launch

class PersonalityDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val personalityId = intent.getIntExtra(
            LoginActivity.EXTRA_PERSONALITY_ID,
            0
        )

        lifecycleScope.launch {
            val database = AppDatabase.getInstance(applicationContext)
            val personality = database.personalityDao().getById(personalityId)

            if (personality == null) {
                binding.tvName.text = getString(R.string.description_unavailable)
                binding.tvYearsOfLife.text = getString(R.string.years_unknown)
                binding.tvEpoch.text = ""
                binding.tvDescription.text = getString(R.string.description_unavailable)
                return@launch
            }

            binding.tvName.text = personality.name
            binding.tvYearsOfLife.text = personality.yearsOfLife
            binding.tvDescription.text = personality.fullDescription
            ImageLoader.loadPersonalityImage(binding.ivPersonality, personality.imageName)

            val epoch = database.epochDao().getById(personality.epochId)
            binding.tvEpoch.text = epoch?.name.orEmpty()
        }
    }
}