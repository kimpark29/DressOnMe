package com.capstone.dressonme.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import com.capstone.dressonme.databinding.ActivitySplashBinding
import com.capstone.dressonme.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val userViewModel by viewModels<UserViewModel>()
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate((layoutInflater))
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportActionBar?.hide()

        checkUserState()
    }

    private fun checkUserState() {
        userViewModel.getUser().observe(this) {
            if (it.token.isEmpty()) {
                binding.imageView.alpha= 0f
                binding.imageView.animate().setDuration(3000L).alpha(1f).withEndAction {
                    val i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    finish()
                }
            } else {
                binding.imageView.alpha= 0f
                binding.imageView.animate().setDuration(2000L).alpha(1f).withEndAction {
                    val moveToListStoryActivity = Intent(this, MainActivity::class.java)
                    startActivity(moveToListStoryActivity)
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                }
            }
        }
    }
}

