package com.example.historyquizru.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.historyquizru.adapter.EpochAdapter
import com.example.historyquizru.data.AppDatabase
import com.example.historyquizru.databinding.ActivityTestsEpochBinding
import kotlinx.coroutines.launch

class TestsEpochActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestsEpochBinding
    private lateinit var adapter: EpochAdapter
    private var userId: Long = 0
    private var userName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestsEpochBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userId = intent.getLongExtra(LoginActivity.EXTRA_USER_ID, 0L)
        userName = intent.getStringExtra(LoginActivity.EXTRA_USER_NAME).orEmpty()

        adapter = EpochAdapter { epoch ->
            startActivity(
                Intent(this, QuizActivity::class.java).apply {
                    putExtra(LoginActivity.EXTRA_EPOCH_ID, epoch.id)
                    putExtra(LoginActivity.EXTRA_USER_ID, userId)
                    putExtra(LoginActivity.EXTRA_USER_NAME, userName)
                }
            )
        }

        binding.rvEpochs.layoutManager = LinearLayoutManager(this)
        binding.rvEpochs.adapter = adapter
        loadEpochs()
    }

    private fun loadEpochs() {
        lifecycleScope.launch {
            val epochs = AppDatabase.getInstance(applicationContext).epochDao().getAll()
            adapter.setItems(epochs)
        }
    }
}