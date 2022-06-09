package com.capstone.dressonme.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.capstone.dressonme.R
import com.capstone.dressonme.databinding.ActivityMainBinding
import com.capstone.dressonme.viewmodel.ProcessViewModel
import com.capstone.dressonme.viewmodel.UserViewModel
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userViewModel by viewModels<UserViewModel>()
    private val processViewModel by viewModels<ProcessViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        checkUserState()
        firstFragment(HomeFragment.newInstance())
        setAction()
    }

    private fun checkUserState() {
        userViewModel.getUser().observe(this) {
            if (it.token.isEmpty()) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                processViewModel.getUserPhotos(it.token, it.userId, object : ApiCallbackString {
                    override fun onResponse(success: Boolean, message: String) {
                        Toast.makeText(this@MainActivity, "Fetch Data Successfully", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun setAction() {
        binding.apply {
            bottomBarNavigation.show(0)
            bottomBarNavigation.add(MeowBottomNavigation.Model(0, R.drawable.ic_home))
            bottomBarNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_camera))
            bottomBarNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_user))
        }

        binding.bottomBarNavigation.setOnClickMenuListener {
            when (it.id) {
                0 -> {
                    changeFragment(HomeFragment.newInstance())
                }
                1 -> {
                    changeFragment(ModelFragment.newInstance())
                }
                2 -> {
                    changeFragment(UserFragment.newInstance())
                }
                else -> {
                    changeFragment(HomeFragment.newInstance())
                }
            }
        }
    }

    private fun firstFragment(fragment: Fragment) {
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.fragmentContainer, fragment)
            .addToBackStack(Fragment::class.java.simpleName).commit()
    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.fragmentContainer, fragment)
            .addToBackStack(Fragment::class.java.simpleName).commit()
    }
}