package com.capstone.dressonme.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.capstone.dressonme.R
import com.capstone.dressonme.databinding.FragmentUserBinding
import com.capstone.dressonme.model.User
import com.capstone.dressonme.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : Fragment() {
    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private val userViewModel : UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkUserState()
        setAction()

    }

    private fun checkUserState() {
        userViewModel.getUser().observe(viewLifecycleOwner) {
            if (it.token.isEmpty()) {
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                setData(it)
            }
        }
    }

    private fun setData(user: User) {
        userViewModel.getUser().observe(viewLifecycleOwner) {
            binding.apply {
                tvUserName.text = it.name
                Glide.with(requireContext()).load(R.drawable.img).into(imgUserProfile)
            }
        }
    }

    private fun setAction() {
        binding.btnLogout.setOnClickListener {
            userViewModel.logout()
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




