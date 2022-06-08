package com.capstone.dressonme.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.capstone.dressonme.R
import com.capstone.dressonme.databinding.ActivityMainBinding
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        firstFragment(HomeFragment.newInstance())
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