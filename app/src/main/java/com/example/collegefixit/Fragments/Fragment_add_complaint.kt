package com.example.collegefixit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.collegefixit.databinding.FragmentAddComplaintBinding
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.viewmodel.ComplaintViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import okhttp3.Response // Correct import for OkHttp response

class AddComplaintFragment : Fragment() {

    private lateinit var binding: FragmentAddComplaintBinding
    private lateinit var viewModel: ComplaintViewModel
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddComplaintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ComplaintViewModel::class.java)

        binding.submitButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            if (title.isNotBlank() && description.isNotBlank()) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val complaint = Complaint(
                        title = title,
                        description = description,
                        userId = userId
                    )
                    viewModel.addComplaint(complaint)

                    // Show success message
                    showToast("Complaint submitted successfully")

                    // Clear input fields and navigate back
                    binding.titleEditText.text?.clear()
                    binding.descriptionEditText.text?.clear()
                    requireActivity().supportFragmentManager.popBackStack()

                    // Send notification to guards
                    sendNotificationToGuards(complaint)
                } else {
                    showToast("User not logged in")
                }
            } else {
                showToast("Please fill in all fields")
            }
        }
    }

    private fun showToast(message: String) {
        if (isAdded) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendNotificationToGuards(complaint: Complaint) {
        if (!isAdded) return

        val json = JSONObject().apply {
            put("title", complaint.title)
            put("description", complaint.description)
            put("userId", complaint.userId)
        }

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://172.70.98.88:3000/complaint") // Use your server's actual URL here
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
