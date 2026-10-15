package com.example.historyquizru.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.historyquizru.R
import com.example.historyquizru.data.AppDatabase
import com.example.historyquizru.data.UserEntity
import com.example.historyquizru.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener { register() }
    }

    private fun register() {
        val name = binding.etName.text?.toString()?.trim().orEmpty()
        val email = binding.etEmail.text?.toString()?.trim().orEmpty()
        val password = binding.etPassword.text?.toString().orEmpty()
        val repeatedPassword = binding.etRepeatPassword.text?.toString().orEmpty()

        if (name.isBlank() || email.isBlank() || password.isBlank() || repeatedPassword.isBlank()) {
            showToast(R.string.validation_fill_all_fields)
            return
        }

        if (password != repeatedPassword) {
            showToast(R.string.validation_passwords_mismatch)
            return
        }

        lifecycleScope.launch {
            try {
                val userDao = AppDatabase.getInstance(applicationContext).userDao()

                if (userDao.findByEmail(email) != null) {
                    showToast(R.string.validation_email_exists)
                    return@launch
                }

                val userId = userDao.insert(
                    UserEntity(
                        name = name,
                        email = email,
                        password = password
                    )
                )

                startActivity(
                    Intent(this@RegisterActivity, MainMenuActivity::class.java).apply {
                        putExtra(LoginActivity.EXTRA_USER_ID, userId)
                        putExtra(LoginActivity.EXTRA_USER_NAME, name)
                    }
                )
                finish()
            } catch (_: Exception) {
                showToast(R.string.validation_registration_error)
            }
        }
    }

    private fun showToast(messageResId: Int) {
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}