package com.capstone.dressonme.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.dressonme.R
import com.capstone.dressonme.databinding.FragmentLoadingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingFragment : Fragment() {
    private var _binding: FragmentLoadingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView.animate().setDuration(5000L).alpha(1f).withEndAction {
            val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.fragmentContainer, CameraFragment()).commit()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ResultFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}