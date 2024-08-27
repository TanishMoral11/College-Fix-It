package com.example.collegefixit.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.collegefixit.databinding.FragmentComplaintDetailsBinding
import com.example.collegefixit.viewmodel.ComplaintViewModel

class ComplaintDetailsFragment : Fragment() {

    private var _binding: FragmentComplaintDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ComplaintViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComplaintDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the ViewModel
        viewModel = ViewModelProvider(requireActivity())[ComplaintViewModel::class.java]

        // Access arguments, e.g., complaintId
        val complaintId = arguments?.getString("complaintId")

        complaintId?.let { id ->
            viewModel.getComplaintById(id).observe(viewLifecycleOwner) { complaint ->
                binding.complaint = complaint
            }
        }

        binding.holdButton.setOnClickListener {
            // Implement hold functionality
        }

        binding.solvedButton.setOnClickListener {
            // Implement solved functionality
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}