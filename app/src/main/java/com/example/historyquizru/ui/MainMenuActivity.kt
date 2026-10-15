package com.example.historyquizru.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.historyquizru.R
import com.example.historyquizru.databinding.ActivityMainMenuBinding

class MainMenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainMenuBinding
    private var userId: Long = 0
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra(LoginActivity.EXTRA_USER_ID, 0L)
        userName = intent.getStringExtra(LoginActivity.EXTRA_USER_NAME).orEmpty()
        binding.tvGreeting.text = getString(R.string.main_menu_greeting, userName)

        binding.btnTests.setOnClickListener {
            startActivity(createUserIntent(TestsEpochActivity::class.java))
        }

        binding.btnPersonalities.setOnClickListener {
            startActivity(createUserIntent(PersonalitiesEpochActivity::class.java))
        }

        binding.btnSettings.setOnClickListener {
            startActivity(createUserIntent(SettingsActivity::class.java))
        }
    }

    private fun createUserIntent(activityClass: Class<*>): Intent {
        return Intent(this, activityClass).apply {
            putExtra(LoginActivity.EXTRA_USER_ID, userId)
            putExtra(LoginActivity.EXTRA_USER_NAME, userName)
        }
    }
}