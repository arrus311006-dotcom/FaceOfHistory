package com.example.historyquizru.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.historyquizru.adapter.EpochAdapter
import com.example.historyquizru.data.AppDatabase
import com.example.historyquizru.databinding.ActivityPersonalitiesEpochBinding
import kotlinx.coroutines.launch

class PersonalitiesEpochActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalitiesEpochBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalitiesEpochBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = EpochAdapter { epoch ->
            startActivity(
                Intent(this, PersonalitiesListActivity::class.java).apply {
                    putExtra(LoginActivity.EXTRA_EPOCH_ID, epoch.id)
                }
            )
        }

        binding.rvEpochs.layoutManager = LinearLayoutManager(this)
        binding.rvEpochs.adapter = adapter

        lifecycleScope.launch {
            val epochs = AppDatabase.getInstance(applicationContext).epochDao().getAll()
            adapter.setItems(epochs)
        }
    }
}