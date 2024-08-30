package com.example.collegefixit.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.collegefixit.Auth.LoginActivity
import com.example.collegefixit.ComplaintsAdapter
import com.example.collegefixit.databinding.FragmentComplaintDetailsBinding
import com.example.collegefixit.guardactivities.GuardMainActivity
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
                if (complaint != null) {
                    complaint.imageUrl?.let{url ->
                        binding.attachedPhotoPreview.visibility = View.VISIBLE
                        Glide.with(this)
                            .load(url)
                            .into(binding.attachedPhotoPreview)
                    } ?: run {
                        binding.attachedPhotoPreview.visibility = View.GONE
                    }
                }
            }
        }

        binding.holdButton.setOnClickListener {
            complaintId?.let { id ->
                viewModel.updateComplaintStatus(id, "On Hold")
//                activity?.supportFragmentManager?.popBackStack()
                navigateToGuardMainActivity()
            }
        }

        binding.solvedButton.setOnClickListener {
            complaintId?.let { id ->
                viewModel.updateComplaintStatus(id, "Solved")
                navigateToGuardMainActivity()
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
    private fun navigateToGuardMainActivity() {
        val intent = android.content.Intent(requireContext(), GuardMainActivity::class.java)
        intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}