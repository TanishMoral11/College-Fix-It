package com.example.collegefixit.guardactivities

import android.os.Bundle
import android.util.Log
import android.view.View
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

        setupRecyclerView()
        observeComplaints()
    }

    private fun setupRecyclerView() {
        binding.complaintsRecyclerView.layoutManager = LinearLayoutManager(this)
        complaintsAdapter = GuardComplaintsAdapter(
            onDetailsClick = { complaint ->
                openComplaintDetails(complaint)
            }
        )
        binding.complaintsRecyclerView.adapter = complaintsAdapter
    }

    private fun observeComplaints() {
        viewModel.complaints.observe(this) { complaints ->
            complaintsAdapter.submitList(complaints)
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Log.e("GuardMainActivity", errorMessage)
            // Handle the error message
        }
    }

    private fun openComplaintDetails(complaint: Complaint) {
        val fragment = ComplaintDetailsFragment.newInstance(complaint.id)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainer, fragment)  // Use the new container ID
            addToBackStack(null)
            commit()
        }

        // Hide the RecyclerView
        binding.complaintsRecyclerView.visibility = View.GONE
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

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            // Show the RecyclerView again when navigating back
            binding.complaintsRecyclerView.visibility = View.VISIBLE
        } else {
            super.onBackPressed()
        }
    }
}
