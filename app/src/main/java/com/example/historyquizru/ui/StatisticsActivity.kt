package com.example.historyquizru.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.historyquizru.R
import com.example.historyquizru.adapter.ResultAdapter
import com.example.historyquizru.data.AppDatabase
import com.example.historyquizru.databinding.ActivityStatisticsBinding
import kotlinx.coroutines.launch

class StatisticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getLongExtra(LoginActivity.EXTRA_USER_ID, 0L)
        val adapter = ResultAdapter()

        binding.rvResults.layoutManager = LinearLayoutManager(this)
        binding.rvResults.adapter = adapter

        lifecycleScope.launch {
            val database = AppDatabase.getInstance(applicationContext)
            val results = database.quizResultDao().getByUser(userId)
            val epochs = database.epochDao().getAll()
            val epochNames = epochs.associate { it.id to it.name }

            adapter.setItems(results, epochNames)

            if (results.isEmpty()) {
                binding.tvSummary.text = getString(R.string.statistics_empty)
            } else {
                val average = results.map { it.correctCount }.average()
                binding.tvSummary.text = getString(
                    R.string.statistics_summary_format,
                    results.size,
                    average,
                    10
                )
            }

            binding.tvSummary.visibility = View.VISIBLE
        }
    }
}