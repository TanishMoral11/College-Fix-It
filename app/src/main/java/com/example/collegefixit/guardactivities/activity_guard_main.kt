package com.example.collegefixit.guardactivities

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegefixit.Fragments.ComplaintDetailsFragment
import com.example.collegefixit.R
import com.example.collegefixit.adapter.GuardComplaintsAdapter
import com.example.collegefixit.databinding.ActivityGuardMainBinding
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.viewmodel.ComplaintViewModel
import com.google.firebase.messaging.FirebaseMessaging

class GuardMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGuardMainBinding
    private lateinit var viewModel: ComplaintViewModel
    private lateinit var complaintsAdapter: GuardComplaintsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToGuardsTopic()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guard_main)

        viewModel = ViewModelProvider(this).get(ComplaintViewModel::class.java)

        binding.complaintsRecyclerView.layoutManager = LinearLayoutManager(this)
        complaintsAdapter = GuardComplaintsAdapter(
            onDetailsClick = { complaint ->
                openComplaintDetails(complaint)
            }
        )


        binding.complaintsRecyclerView.adapter = complaintsAdapter

        viewModel.complaints.observe(this) { complaints ->
            complaintsAdapter.submitList(complaints)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Log.e(TAG, errorMessage)
            // Handle the error message
        }
    }
    private fun openComplaintDetails(complaint: Complaint) {
        val fragment = ComplaintDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("complaintId", complaint.id)
            }
        }
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun subscribeToGuardsTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("guards_notifications")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Guard subscribed to notifications topic")
                } else {
                    Log.e("FCM", "Failed to subscribe guard to notifications topic", task.exception)
                }
            }
    }
}
