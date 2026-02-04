package com.example.collegefixit.Fragments

import android.content.Context
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
                    // Set author name or "Anonymous"
                    val authorText = if (complaint.isAnonymous || complaint.authorName.isEmpty()) {
                        "By : Anonymous"
                    } else {
                        val year = if (complaint.userYear.isNotEmpty()) {
                            complaint.userYear
                        } else {
                            extractYearFromUserId(complaint.userId)
                        }
                        
                        if (year.isNotEmpty()) {
                            "By : ${complaint.authorName} ( ${year} )"
                        } else {
                            "By : ${complaint.authorName}"
                        }
                    }
                    binding.authorNameTextView.text = authorText
                    complaint.imageUrl?.let { url ->
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

        // Set up back button
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Check user role to show/hide guard buttons
        val sharedPref = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val userRole = sharedPref.getString("USER_ROLE", "student") ?: "student"

        // Hide guard buttons if user is a student
        if (userRole == "student") {
            binding.holdButton.visibility = View.GONE
            binding.solvedButton.visibility = View.GONE
        } else {
            // Show buttons only for guards
            binding.holdButton.visibility = View.VISIBLE
            binding.solvedButton.visibility = View.VISIBLE
        }

        // Guard buttons - only show if this is being used in guard context
        binding.holdButton.setOnClickListener {
            complaintId?.let { id ->
                viewModel.updateComplaintStatus(id, "On Hold")
                parentFragmentManager.popBackStack()
            }
        }

        binding.solvedButton.setOnClickListener {
            complaintId?.let { id ->
                viewModel.updateComplaintStatus(id, "Solved")
                parentFragmentManager.popBackStack()
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

    private fun extractYearFromUserId(userId: String): String {
        return try {
            // If userYear is not stored, try to extract from userId if it contains roll number
            if (userId.contains("@")) {
                val rollNo = userId.substring(0, userId.indexOf('@'))
                val admissionYear = rollNo.substring(3, 7).toInt()
                val calendar = java.util.Calendar.getInstance()
                val currentYear = calendar.get(java.util.Calendar.YEAR)
                val currentMonth = calendar.get(java.util.Calendar.MONTH)

                val acadYear = if (currentMonth >= java.util.Calendar.AUGUST) {
                    currentYear - admissionYear + 1
                } else {
                    currentYear - admissionYear
                }

                val suffix = when {
                    acadYear in 11..13 -> "th"
                    acadYear % 10 == 1 -> "st"
                    acadYear % 10 == 2 -> "nd"
                    acadYear % 10 == 3 -> "rd"
                    else -> "th"
                }
                "B.Tech ${acadYear}${suffix} year"
            } else {
                ""
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun navigateToGuardMainActivity() {
        val intent = android.content.Intent(requireContext(), GuardMainActivity::class.java)
        intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
