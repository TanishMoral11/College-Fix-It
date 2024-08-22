package com.example.collegefixit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.collegefixit.databinding.FragmentAddComplaintBinding
import com.example.collegefixit.viewmodel.ComplaintViewModel
import com.example.collegefixit.model.Complaint
import com.google.firebase.auth.FirebaseAuth

class AddComplaintFragment : Fragment() {

    private lateinit var binding: FragmentAddComplaintBinding
    private lateinit var viewModel: ComplaintViewModel

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
                    // Clear input fields and navigate back
                } else {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle invalid input
            }
        }
    }
}
