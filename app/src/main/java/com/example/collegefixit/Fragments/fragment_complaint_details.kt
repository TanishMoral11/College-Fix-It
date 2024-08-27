package com.example.collegefixit.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.collegefixit.ComplaintsAdapter
import com.example.collegefixit.databinding.FragmentComplaintDetailsBinding
import com.example.collegefixit.viewmodel.ComplaintViewModel

class ComplaintDetailsFragment : Fragment() {

    private var _binding: FragmentComplaintDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ComplaintViewModel
    private lateinit var adapter: ComplaintsAdapter
    private var complaintId: String? = null

    companion object {
        private const val ARG_COMPLAINT_ID = "complaint_id"

        fun newInstance(complaintId: String): ComplaintDetailsFragment {
            return ComplaintDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COMPLAINT_ID, complaintId)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            complaintId = it.getString(ARG_COMPLAINT_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComplaintDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ComplaintViewModel::class.java)

        adapter = ComplaintsAdapter(
            onUpvoteClick = { complaintId ->
                viewModel.toggleUpvote(complaintId)
            },
            onDeleteClick = { complaintId ->
                viewModel.deleteComplaint(complaintId)
            }
        )

        complaintId?.let { id ->
            viewModel.getComplaintById(id).observe(viewLifecycleOwner) { complaint ->
                binding.complaint = complaint
            }
        }

        binding.holdButton.setOnClickListener {
            complaintId?.let { id ->
                viewModel.updateComplaintStatus(id, "On Hold")
                activity?.supportFragmentManager?.popBackStack()
            }
        }

        binding.solvedButton.setOnClickListener {
            complaintId?.let { id ->
                viewModel.updateComplaintStatus(id, "Solved")
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getAdapter(): ComplaintsAdapter {
        return adapter
    }
}