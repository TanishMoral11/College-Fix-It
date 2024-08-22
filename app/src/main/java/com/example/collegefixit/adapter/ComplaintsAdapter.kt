package com.example.collegefixit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.collegefixit.databinding.ItemComplaintBinding
import com.example.collegefixit.model.Complaint
import com.google.firebase.auth.FirebaseAuth


class ComplaintsAdapter(
    private val onUpvoteClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit // Callback for deleting complaints
) : ListAdapter<Complaint, ComplaintsAdapter.ComplaintViewHolder>(ComplaintDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val binding = ItemComplaintBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = getItem(position)
        holder.bind(complaint)
    }

    inner class ComplaintViewHolder(private val binding: ItemComplaintBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(complaint: Complaint) {
            binding.complaint = complaint

            // Set the current upvote count
            binding.upvoteCount.text = complaint.upvotes.toString()

            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

            // Check if the current user has upvoted this complaint
            val hasUpvoted = currentUserId in complaint.upvotedBy
            binding.upvoteButton.isSelected = hasUpvoted

            // Handle upvote button click
            binding.upvoteButton.setOnClickListener {
                onUpvoteClick(complaint.id)
            }

            // Show the delete button only if the current user created the complaint
            if (complaint.userId == currentUserId) {
                binding.deleteButton.visibility = View.VISIBLE
                binding.deleteButton.setOnClickListener {
                    onDeleteClick(complaint.id)
                }
            } else {
                binding.deleteButton.visibility = View.GONE
            }
        }
    }

    private class ComplaintDiffCallback : DiffUtil.ItemCallback<Complaint>() {
        override fun areItemsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
            return oldItem == newItem
        }
    }
}
