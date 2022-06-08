package com.capstone.dressonme.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.capstone.dressonme.databinding.ActivitySplashBinding
import com.capstone.dressonme.helper.ViewModelFactory
import com.capstone.dressonme.local.User
import com.capstone.dressonme.local.UserPreference
import com.capstone.dressonme.ui.viewmodel.UserViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var user: User
    private lateinit var viewModel: UserViewModel
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate((layoutInflater))
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        supportActionBar?.hide()
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore))
        )[UserViewModel::class.java]

        viewModel.getUser().observe(this) {
            user = User(
                it.userId,
                it.token
            )
        }
        viewModel.getUser().observe(this) {
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
//                    moveToListStoryActivity.putExtra(MainActivity.EXTRA_USER, user)
                    startActivity(moveToListStoryActivity)
                    overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
                    finish()
                }
            }
        }
        binding.imageView.alpha= 0f
        binding.imageView.animate().setDuration(3000L).alpha(1f).withEndAction {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }
}

