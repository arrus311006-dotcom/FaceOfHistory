package com.example.historyquizru.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.historyquizru.adapter.PersonalityAdapter
import com.example.historyquizru.data.AppDatabase
import com.example.historyquizru.databinding.ActivityPersonalitiesListBinding
import kotlinx.coroutines.launch

class PersonalitiesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalitiesListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalitiesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val epochId = intent.getIntExtra(LoginActivity.EXTRA_EPOCH_ID, 0)
        val adapter = PersonalityAdapter { personality ->
            startActivity(
                Intent(this, PersonalityDetailActivity::class.java).apply {
                    putExtra(LoginActivity.EXTRA_PERSONALITY_ID, personality.id)
                }
            )
        }

        binding.rvPersonalities.layoutManager = LinearLayoutManager(this)
        binding.rvPersonalities.adapter = adapter

        lifecycleScope.launch {
            val personalities = AppDatabase.getInstance(applicationContext)
                .personalityDao()
                .getByEpoch(epochId)
            adapter.setItems(personalities)
        }
    }
}