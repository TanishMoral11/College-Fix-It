package com.example.collegefixit.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegefixit.ComplaintsAdapter
import com.example.collegefixit.R
import com.example.collegefixit.databinding.FragmentComplaintsListBinding
import com.example.collegefixit.viewmodel.ComplaintViewModel

class ComplaintsListFragment : Fragment() {

    private var _binding: FragmentComplaintsListBinding? = null
    private val binding get() = _binding!!
    private lateinit var complaintViewModel: ComplaintViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComplaintsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        complaintViewModel = ViewModelProvider(requireActivity()).get(ComplaintViewModel::class.java)

        // Set up RecyclerView
        binding.complaintsRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ComplaintsAdapter(
            onUpvoteClick = { complaintId ->
                complaintViewModel.toggleUpvote(complaintId)
            },
            onDeleteClick = { complaintId ->
                complaintViewModel.deleteComplaint(complaintId)
            }
        )
        binding.complaintsRecyclerView.adapter = adapter

        // Observe the LiveData from ViewModel and update UI in real-time
        complaintViewModel.complaints.observe(viewLifecycleOwner) { complaints ->
            adapter.submitList(complaints)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
