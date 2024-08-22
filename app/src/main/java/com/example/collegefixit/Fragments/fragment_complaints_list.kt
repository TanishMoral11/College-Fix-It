package com.example.collegefixit.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collegefixit.ComplaintsAdapter
import com.example.collegefixit.databinding.FragmentComplaintsListBinding
import com.example.collegefixit.viewmodel.ComplaintViewModel

class ComplaintsListFragment : Fragment() {

    private lateinit var binding: FragmentComplaintsListBinding
    private lateinit var viewModel: ComplaintViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComplaintsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ComplaintViewModel::class.java)

        // Set up RecyclerView
        binding.complaintsRecyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = ComplaintsAdapter { complaintId ->
            // Handle upvote click
            viewModel.toggleUpvote(complaintId)
        }
        binding.complaintsRecyclerView.adapter = adapter

        // Observe the LiveData from ViewModel and update UI in real-time
        viewModel.complaints.observe(viewLifecycleOwner, { complaints ->
            adapter.submitList(complaints)
        })
    }
}
