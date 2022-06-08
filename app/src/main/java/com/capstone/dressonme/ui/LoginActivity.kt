package com.capstone.dressonme.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.dressonme.databinding.ActivityLoginBinding
import com.capstone.dressonme.helper.ViewModelFactory
import com.capstone.dressonme.local.User
import com.capstone.dressonme.local.UserPreference
import com.capstone.dressonme.ui.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var user: User
    private lateinit var LogViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        setupViewModel()
        setAction()
    }

    private fun setupViewModel() {
        LogViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[LoginViewModel::class.java]

        LogViewModel.getUser().observe(this) {
            user = User(
                it.userId,
                it.token
            )
        }
    }

    private fun setAction() {
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPass.text.toString()

            LogViewModel.login(email, password, object : ApiCallbackString {
                override fun onResponse(success: Boolean, message: String) {
                    if (success) {
                        Toast.makeText(this@LoginActivity, "Sign Up Success", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(this@LoginActivity,
                            "Sign up failed $message",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }
        binding.tvReg.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}