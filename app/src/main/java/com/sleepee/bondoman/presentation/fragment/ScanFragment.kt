package com.sleepee.bondoman.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.sleepee.bondoman.Manifest
import com.sleepee.bondoman.R
import com.sleepee.bondoman.databinding.FragmentScanBinding

const val CAMERA_REQUEST_CODE = 100
const val IMAGE_REQUEST_CODE = 101

class ScanFragment: Fragment() {

    private lateinit var binding: FragmentScanBinding
    private var cameraPermissions = listOf<String>()
    private var imagePermissions = listOf<String>()
    private var imageUri : Uri ? = null
    private var galleryActivityResultLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    if (data != null) {
                        imageUri = data.data
                        binding.scannedImage.setImageURI(imageUri)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Cancelled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPermissions()
        setupUI()
    }

    private fun setupPermissions() {
        cameraPermissions = arrayListOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES)
        imagePermissions = arrayListOf(android.Manifest.permission.READ_MEDIA_IMAGES)
    }

    private fun setupUI() {
        binding.cameraButton.setOnClickListener {

        }

        binding.galleryButton.setOnClickListener {

        }

        binding.scanButton.setOnClickListener {

        }
    }

    private fun grabImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        galleryActivityResultLauncher.launch(intent)
    }

    private fun takeImageFromCamera() {
        
    }


}







