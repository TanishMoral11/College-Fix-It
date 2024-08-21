package com.example.collegefixit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegefixit.databinding.ActivityMainBinding
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.viewmodel.ComplaintViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: ComplaintViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        //for disable the dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.complaintsRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ComplaintsAdapter { complaintId ->
            // Handle upvote click
            viewModel.upvoteComplaint(complaintId)
        }
        binding.complaintsRecyclerView.adapter = adapter

        // Observe the LiveData from ViewModel and update UI in real-time
        viewModel.complaints.observe(this, Observer { complaints ->
            adapter.submitList(complaints)
        })


        viewModel.errorMessage.observe(this, Observer { errorMessage ->
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Handle submit button click to add a new complaint
        // Handle submit button click to add a new complaint
        binding.submitButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            if (title.isNotBlank() && description.isNotBlank()) {
                val complaint = Complaint(title = title, description = description)
                viewModel.addComplaint(complaint)
                binding.titleEditText.text.clear()
                binding.descriptionEditText.text.clear()
            } else {
                Toast.makeText(this, "Please fill in both title and description", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
