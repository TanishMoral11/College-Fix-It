package com.example.collegefixit

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.collegefixit.databinding.FragmentAddComplaintBinding
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.viewmodel.ComplaintViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.util.*

class AddComplaintFragment : Fragment() {

    private lateinit var binding: FragmentAddComplaintBinding
    private lateinit var viewModel: ComplaintViewModel
    private val client = OkHttpClient()
    private var imageUri: Uri? = null
    private val storageRef = FirebaseStorage.getInstance().reference

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                showToast("Camera permission is required to take photos")
            }
        }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                imageUri = result.data?.data
                displaySelectedImage()
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                bitmap?.let {
                    val path = MediaStore.Images.Media.insertImage(
                        requireActivity().contentResolver,
                        it,
                        "CameraImage",
                        null
                    )
                    imageUri = Uri.parse(path)
                    displaySelectedImage()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddComplaintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ComplaintViewModel::class.java)

        binding.submitButton.setOnClickListener {
            submitComplaint()
        }

        binding.attachPhotoButton.setOnClickListener {
            showPhotoOptionsDialog()
        }
    }

    private fun submitComplaint() {
        val title = binding.titleEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()

        if (title.isBlank() || description.isBlank()) {
            showToast("Please fill in all fields")
            return
        }

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            showToast("User not logged in")
            return
        }

        if (imageUri != null) {
            val imagePath = "complaints/${UUID.randomUUID()}.jpg"
            uploadImageAndSubmitComplaint(imageUri!!, imagePath, userId, title, description)
        } else {
            submitComplaintWithoutImage(userId, title, description)
        }
    }

    private fun uploadImageAndSubmitComplaint(uri: Uri, path: String, userId: String, title: String, description: String) {
        val ref = storageRef.child(path)
        ref.putFile(uri).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { downloadUrl ->
                val complaint = Complaint(
                    title = title,
                    description = description,
                    userId = userId,
                    imageUrl = downloadUrl.toString()
                )
                viewModel.addComplaint(complaint)
                showToast("Complaint submitted successfully with image")
                clearFields()
                sendNotificationToGuards(complaint)
            }
        }.addOnFailureListener { e ->
            showToast("Failed to upload image: ${e.message}")
        }
    }

    private fun submitComplaintWithoutImage(userId: String, title: String, description: String) {
        val complaint = Complaint(
            title = title,
            description = description,
            userId = userId
        )
        viewModel.addComplaint(complaint)
        showToast("Complaint submitted successfully")
        clearFields()
        sendNotificationToGuards(complaint)
    }

    private fun showPhotoOptionsDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Photo Option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> checkCameraPermissionAndOpen()
                    1 -> openImagePicker()
                }
            }
            .show()
    }

    private fun checkCameraPermissionAndOpen() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                showCameraPermissionRationale()
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun showCameraPermissionRationale() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Camera Permission Required")
            .setMessage("We need camera permission to take photos for your complaint. Please grant the permission in the next dialog.")
            .setPositiveButton("OK") { _, _ ->
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun displaySelectedImage() {
        binding.attachedPhotoPreview.setImageURI(imageUri)
        binding.attachedPhotoPreview.visibility = View.VISIBLE
    }

    private fun clearFields() {
        binding.titleEditText.text?.clear()
        binding.descriptionEditText.text?.clear()
        binding.locationEditText.text?.clear()
        binding.attachedPhotoPreview.visibility = View.GONE
        imageUri = null
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun sendNotificationToGuards(complaint: Complaint) {
        val json = JSONObject().apply {
            put("title", complaint.title)
            put("description", complaint.description)
            put("userId", complaint.userId)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://172.70.101.60/complaint")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    showToast("Failed to send notification: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                activity?.runOnUiThread {
                    if (response.isSuccessful) {
                        showToast("Notification sent to guards!")
                    } else {
                        showToast("Failed to send notification: ${response.message}")
                    }
                }
            }
        })
    }
}
