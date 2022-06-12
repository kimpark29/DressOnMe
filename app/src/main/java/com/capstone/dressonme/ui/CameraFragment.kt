package com.capstone.dressonme.ui

import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstone.dressonme.R
import com.capstone.dressonme.databinding.FragmentCameraBinding
import com.capstone.dressonme.helper.ApiCallbackString
import com.capstone.dressonme.helper.reduceFileImage
import com.capstone.dressonme.helper.rotateBitmap
import com.capstone.dressonme.viewmodel.ProcessViewModel
import com.capstone.dressonme.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class CameraFragment : Fragment() {
    private val userViewModel by viewModels<UserViewModel>()
    private val processViewModel by viewModels<ProcessViewModel>()
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private lateinit var result: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            addImg()

            val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.fragmentContainer, HomeFragment()).commit()
        }

        binding.btnCamera.setOnClickListener { startCameraX() }
        binding.cardView.setOnClickListener { startCameraX() }
        back()
    }

    private fun back() {
        binding.btnBack.setOnClickListener {
//            processViewModel.delete()
            val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
            fragmentTransition.replace(R.id.fragmentContainer, ModelFragment()).commit()
        }
    }


    private fun addImg() {
        if (getFile != null) {
            val imgFile = reduceFileImage(getFile as File)

            processViewModel.getProcess().observe(viewLifecycleOwner) { process ->
                Toast.makeText(context, process._id, Toast.LENGTH_SHORT).show()
                userViewModel.getUser().observe(viewLifecycleOwner) { user ->
                    processViewModel.updateResult(user.token,
                        process._id,
                        imgFile,
                        object : ApiCallbackString {
                            override fun onResponse(success: Boolean, message: String) {
                                Toast.makeText(context,
                                    message,
                                    Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            }
        }
    }

    private fun startCameraX() {
        val intent = Intent(activity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )

            binding.imagePreview.setImageBitmap(result)
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            CameraFragment().apply {
                arguments = Bundle().apply {}
            }

        const val CAMERA_X_RESULT = 200
    }
}