package com.sleepee.bondoman.presentation.fragment

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.vision.common.InputImage
import com.sleepee.bondoman.data.model.LoginRequest
import com.sleepee.bondoman.data.model.TransactionDao
import com.sleepee.bondoman.data.model.TransactionDatabase
import com.sleepee.bondoman.data.util.TokenManager
import com.sleepee.bondoman.data.util.TransactionUtils
import com.sleepee.bondoman.databinding.FragmentScanBinding
import com.sleepee.bondoman.network.api.LoginApiService
import com.sleepee.bondoman.network.api.RetrofitClient
import com.sleepee.bondoman.network.api.ScanApiService
import com.sleepee.bondoman.presentation.activity.AddTransactionActivity
import com.sleepee.bondoman.presentation.activity.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import kotlin.concurrent.thread

const val CAMERA_REQUEST_CODE = 100
const val IMAGE_REQUEST_CODE = 101

class ScanFragment: Fragment() {

    private lateinit var binding: FragmentScanBinding
    private var cameraPermissions = listOf<String>()
    private var imagePermissions = listOf<String>()
    private var imageUri : Uri? = null
    private lateinit var location: String
    private var token: String = "Bearer ${TokenManager.getToken()}"
    private lateinit var outputFile: File
    private lateinit var database: TransactionDatabase
    private val transactionDao: TransactionDao by lazy {
        database.getTransactionDao()
    }
    private val scanService : ScanApiService = RetrofitClient.Instance.create(ScanApiService::class.java)
    private var galleryActivityResultLauncher : ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    if (data != null) {
                        imageUri = data.data
                        binding.scannedImage.setImageURI(imageUri)
                        binding.addTransactionButton.visibility = View.VISIBLE
                        binding.cameraButton.text = "Retake Camera"
                        binding.galleryButton.text = "Retake Gallery"
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

    private var cameraActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback {result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                if (data != null) {
                    binding.scannedImage.setImageURI(imageUri)
                    binding.addTransactionButton.visibility = View.VISIBLE
                    binding.cameraButton.text = "Retake Camera"
                    binding.galleryButton.text = "Retake Gallery"
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Okay",
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

        database = TransactionDatabase.getDatabase(requireContext())

        val sh = requireActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE)
        location = sh.getString("location", "").toString()z

        setupPermissions()
        setupUI()
    }


    // Below are the permissions needed to use image from camera and gallery
    private fun setupPermissions() {
        cameraPermissions = arrayListOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_MEDIA_IMAGES)
        imagePermissions = arrayListOf(android.Manifest.permission.READ_MEDIA_IMAGES)
    }

    private fun setupUI() {

        if (imageUri == null) {
            binding.addTransactionButton.visibility = View.GONE
        }

        binding.cameraButton.setOnClickListener {
            if (checkCameraPermission()) {
                takeImageFromCamera()
            } else {
                requestCameraPermission()
            }
        }

        binding.galleryButton.setOnClickListener {
            if (checkImagePermissions()) {
                grabImageFromGallery()
            } else {
                requestImagePermission()
            }
        }

        binding.addTransactionButton.setOnClickListener {
            if (imageUri == null) {
                Toast.makeText(
                    requireContext(),
                    "No image selected. Please grab an image from camera/gallery",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                detectResultFromImage()
            }
        }


    }

    private fun detectResultFromImage() {
        try {
            changeUriToJpg()
            sendImage()



        } catch (e: Exception) {

        }
    }

    private fun sendImage() {
        val filePart = MultipartBody.Part.createFormData("file", outputFile.name, RequestBody.create(
            MediaType.parse("image/*"), outputFile))
        lifecycleScope.launch {
            val res = withContext(Dispatchers.IO) {
                try {
                    scanService.uploadAttachment(token, filePart)
                } catch (e: Exception) {
                    Log.e("ScanActivity", "Scan failed: ${e.message}")
                    null
                }
            }
            Log.d("scanresults", "results: $res")
            if (res != null && res.isSuccessful && res.body() != null){
                val items = res.body()!!.itemsList
                Log.d("ScanResults", "Scan success with ${items.items[0]}")
                for (dummyTransaction in items.items) {
                    val transaction = TransactionUtils.convertToTransaction(dummyTransaction.name, dummyTransaction.price.toInt(), TransactionUtils.getCurrentDate(), location, "Pemasukan")
                    thread {
                        transactionDao.createTransaction(transaction)
                    }
                }
                Toast.makeText(requireContext(), "Scanned transactions added!", Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Scan failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun changeUriToJpg() {
        val inputStream = imageUri?.let { requireContext().contentResolver.openInputStream(it) }
        val bitmap = BitmapFactory.decodeStream(inputStream)

        val outputDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        outputFile = File(outputDir, "file.jpg")

        val outputStream = FileOutputStream(outputFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        outputStream.close()
        inputStream?.close()
    }

    private fun grabImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType("image/*")
        galleryActivityResultLauncher.launch(intent)
    }

    private fun takeImageFromCamera() {
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE, "Sample title")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Sample description")
        imageUri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    private fun checkImagePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestImagePermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            imagePermissions.toTypedArray(), IMAGE_REQUEST_CODE)
    }

    private fun checkCameraPermission(): Boolean {
        val isImage = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
        val isCamera = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        return isImage && isCamera
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
            cameraPermissions.toTypedArray(), IMAGE_REQUEST_CODE)
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
                val imageAccepted = (grantResults[1] == PackageManager.PERMISSION_GRANTED)
                val storageAccepted = (grantResults[2] == PackageManager.PERMISSION_GRANTED)
                if (cameraAccepted && imageAccepted && storageAccepted) {
                    takeImageFromCamera()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Camera, image, and storage permissions are required",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else if (requestCode == IMAGE_REQUEST_CODE) {
            val imageAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            val storageAccepted = (grantResults[1] == PackageManager.PERMISSION_GRANTED)
            if (imageAccepted && storageAccepted) {
                grabImageFromGallery()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Image and storage permissions are required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }


}







