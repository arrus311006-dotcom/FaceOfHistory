package com.example.historyquizru.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.historyquizru.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private var userId: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra(LoginActivity.EXTRA_USER_ID, 0L)

        binding.btnStatistics.setOnClickListener {
            startActivity(
                Intent(this, StatisticsActivity::class.java).apply {
                    putExtra(LoginActivity.EXTRA_USER_ID, userId)
                }
            )
        }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }
}