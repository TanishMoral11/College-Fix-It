package com.example.collegefixit

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegefixit.databinding.ActivityMainBinding
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.viewmodel.ComplaintViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: ComplaintViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private val userId = "defaultUser"  // Replace with actual user ID in a real application

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.complaintsRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ComplaintsAdapter(viewModel)
        binding.complaintsRecyclerView.adapter = adapter

        // Observe the LiveData from ViewModel and update UI in real-time
        viewModel.complaints.observe(this, Observer { complaints ->
            adapter.submitList(complaints)
        })

        // Handle submit button click to add a new complaint
        binding.submitButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val complaint = Complaint(title = title, description = description)
            viewModel.addComplaint(complaint)
        }
    }
}
