package com.capstone.dressonme.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.capstone.dressonme.databinding.FragmentUserBinding
import com.capstone.dressonme.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root


    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            userViewModel.logout()
            startActivity(Intent(activity, LoginActivity::class.java))
            activity?.finish()
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() =
            UserFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}




