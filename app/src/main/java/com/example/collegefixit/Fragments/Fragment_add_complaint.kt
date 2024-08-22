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
                val complaint = Complaint(title = title, description = description)
                viewModel.addComplaint(complaint)
                Toast.makeText(context, "Complaint submitted", Toast.LENGTH_SHORT).show()
                binding.titleEditText.text.clear()
                binding.descriptionEditText.text.clear()
            } else {
                Toast.makeText(context, "Please fill in both title and description", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
