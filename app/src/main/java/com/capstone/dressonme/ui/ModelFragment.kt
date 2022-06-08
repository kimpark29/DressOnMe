package com.capstone.dressonme.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.dressonme.R
import com.capstone.dressonme.databinding.FragmentModelBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ModelFragment : Fragment() {

    private var _binding: FragmentModelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentModelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.fragmentContainer, CameraFragment()).commit()
        }
    }

    companion object {

        fun newInstance() =
            ModelFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}