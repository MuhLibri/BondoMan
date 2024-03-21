package com.sleepee.bondoman.presentation.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.controls.Mode
import com.otaliastudios.cameraview.overlay.OverlayLayout
import com.sleepee.bondoman.databinding.FragmentTwibbonBinding
import com.sleepee.bondoman.presentation.activity.RetakeTwibbonActivity
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream


class TwibbonFragment: Fragment() {
    private lateinit var binding: FragmentTwibbonBinding
    private lateinit var camera: CameraView
    private lateinit var imageBitmap : Bitmap
    private var cameraPermissions = listOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTwibbonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        camera = binding.camera
        camera.setLifecycleOwner(viewLifecycleOwner)
        camera.mode = Mode.PICTURE

        setupPermissions()

        if (checkCameraPermission()) {
            setupCamera()
            binding.takePicButton.setOnClickListener {
                camera.takePictureSnapshot()
            }
        } else {
            requestCameraPermission()
        }



    }

    override fun onResume() {
        super.onResume()
        camera.open()
    }

    override fun onPause() {
        super.onPause()
        camera.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.destroy()
    }

    private fun setupPermissions() {
        cameraPermissions = arrayListOf(android.Manifest.permission.CAMERA)
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            cameraPermissions.toTypedArray(), IMAGE_REQUEST_CODE)
    }

    private fun setupCamera() {
        camera.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                result.toBitmap(300, 300, BitmapCallback {
                    if (it != null) {
                        imageBitmap = it
                        val baos = ByteArrayOutputStream()
                        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
                        val b = baos.toByteArray()
                        val intent = Intent(requireContext(), RetakeTwibbonActivity::class.java)
                        intent.putExtra("TwibbonPhoto", b)
                        startActivity(intent)
                    }
                })
            }
        })
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty()) {
                val cameraAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                if (cameraAccepted) {
                    setupCamera()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Camera permissions are required",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}