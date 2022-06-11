package com.capstone.dressonme.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.capstone.dressonme.R
import com.capstone.dressonme.databinding.FragmentModelBinding
import com.capstone.dressonme.helper.ApiCallbackString
import com.capstone.dressonme.helper.reduceFileImage
import com.capstone.dressonme.helper.uriToFile
import com.capstone.dressonme.viewmodel.ProcessViewModel
import com.capstone.dressonme.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@AndroidEntryPoint
class ModelFragment : Fragment() {

    private val processViewModel by viewModels<ProcessViewModel>()
    private val userViewModel by viewModels<UserViewModel>()
    private var _binding: FragmentModelBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentModelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUserProcess()
        setAction()
    }

    private fun checkUserProcess() {
        processViewModel.getProcess().observe(viewLifecycleOwner) {
            if (it != null && it._id != "") {
                val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()
                fragmentTransition.replace(R.id.fragmentContainer, CameraFragment()).commit()
            }
        }
    }

    private fun setAction() {
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.cardView.setOnClickListener { startGallery() }
        binding.btnNext.setOnClickListener {
            addImageModel()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireActivity())

            getFile = myFile
            binding.imagePreview.setImageURI(selectedImg)
        }
    }

    private fun addImageModel() {
        if (getFile != null) {
            val imgFile = reduceFileImage(getFile as File)

            userViewModel.getUser().observe(viewLifecycleOwner) {
                processViewModel.startProcess(it.token,
                    it.userId,
                    imgFile,
                    object : ApiCallbackString {
                        override fun onResponse(success: Boolean, message: String) {

                            processViewModel.userProcess.observe(viewLifecycleOwner) { process ->
                                processViewModel.saveProcess(process)
                                val fragmentTransition =
                                    requireActivity().supportFragmentManager.beginTransaction()
                                fragmentTransition.replace(R.id.fragmentContainer, CameraFragment())
                                    .commit()
                            }

                            processViewModel.triggerCloudRun(it.token, object : ApiCallbackString {
                                override fun onResponse(success: Boolean, message: String) {
                                    Toast.makeText(activity, message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            })
                        }
                    })
            }

        } else {
            Toast.makeText(activity,
                "Silahkan masukkan gambar terlebih dahulu.",
                Toast.LENGTH_SHORT).show()
        }
    }


    companion object {
        fun newInstance() =
            ModelFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}