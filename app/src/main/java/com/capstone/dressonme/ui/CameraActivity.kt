package com.capstone.dressonme.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.format.DateFormat
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.capstone.dressonme.R
import com.capstone.dressonme.databinding.ActivityCameraBinding
import com.capstone.dressonme.helper.ApiCallbackString
import com.capstone.dressonme.helper.createFile
import com.capstone.dressonme.ui.CameraFragment.Companion.CAMERA_X_RESULT
import com.capstone.dressonme.viewmodel.ProcessViewModel
import com.capstone.dressonme.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {

    private val userViewModel by viewModels<UserViewModel>()
    private val processViewModel by viewModels<ProcessViewModel>()
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getUserProcess()
        getData()

        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.captureImage.setOnClickListener { takePhoto() }
        binding.switchCamera.setOnClickListener {
            cameraSelector =
                if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) CameraSelector.DEFAULT_FRONT_CAMERA
                else CameraSelector.DEFAULT_BACK_CAMERA
            startCamera()
        }

    }

    private fun getUserProcess() {

        userViewModel.getUser().observe(this) {
            processViewModel.getProcess().observe(this) { processDetail ->
                Toast.makeText(this@CameraActivity,
                    "IDnya :  ${processDetail.linkFiltering}",
                    Toast.LENGTH_SHORT)
                    .show()

                processViewModel.getUserProcess(it.token,
                    processDetail._id,
                    object : ApiCallbackString {
                        override fun onResponse(success: Boolean, message: String) {
                            processViewModel.userProcess.observe(this@CameraActivity) {
                                processViewModel.saveProcess(it)
                                if (it.linkFiltering != "") {
                                    Toast.makeText(this@CameraActivity, message, Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        }
                    })
            }
        }


    }

    private fun getData() {
        processViewModel.userProcess.observe(this) {
            Glide.with(this@CameraActivity)
                .load(Uri.parse(it.linkFiltering))
                .into(binding.gambar)
        }

    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Toast.makeText(
                        this@CameraActivity,
                        getString(R.string.error_pick_image),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val intent = Intent()
                    intent.putExtra("picture", photoFile)
                    intent.putExtra(
                        "isBackCamera",
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    setResult(CAMERA_X_RESULT, intent)
                    finish()
                }
            }
        )
    }

    private fun startCamera() {

        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch (exc: Exception) {
                Toast.makeText(
                    this@CameraActivity,
                    getString(R.string.error_showing_camera),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun hideSystemUI() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}