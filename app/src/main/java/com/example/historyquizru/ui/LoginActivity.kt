package com.example.historyquizru.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.historyquizru.R
import com.example.historyquizru.data.AppDatabase
import com.example.historyquizru.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { login() }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()

        if (email.isBlank() || password.isBlank()) {
            showToast(R.string.validation_fill_all_fields)
            return
        }

        lifecycleScope.launch {
            try {
                val user = AppDatabase.getInstance(applicationContext)
                    .userDao()
                    .findByEmail(email)

                if (user == null || user.password != password) {
                    showToast(R.string.validation_invalid_credentials)
                    return@launch
                }

                startActivity(
                    Intent(this@LoginActivity, MainMenuActivity::class.java).apply {
                        putExtra(EXTRA_USER_ID, user.id)
                        putExtra(EXTRA_USER_NAME, user.name)
                    }
                )
                finish()
            } catch (_: Exception) {
                showToast(R.string.validation_login_error)
            }
        }
    }

    private fun showToast(messageResId: Int) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USER_ID = "userId"
        const val EXTRA_USER_NAME = "userName"
        const val EXTRA_EPOCH_ID = "epochId"
        const val EXTRA_PERSONALITY_ID = "personalityId"
    }
}