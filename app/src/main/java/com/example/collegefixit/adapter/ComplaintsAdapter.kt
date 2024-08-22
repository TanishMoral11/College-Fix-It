package com.example.collegefixit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.collegefixit.databinding.ItemComplaintBinding
import com.example.collegefixit.model.Complaint
import com.google.firebase.auth.FirebaseAuth

class ComplaintsAdapter(
    private val onUpvoteClick: (String) -> Unit
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

            // Check if the current user has upvoted this complaint
            val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
            val hasUpvoted = currentUserId in complaint.upvotedBy

            // Update the upvote button appearance based on the user's upvote status
            binding.upvoteButton.isSelected = hasUpvoted

            // Handle upvote button click
            binding.upvoteButton.setOnClickListener {
                onUpvoteClick(complaint.id)  // Trigger the callback to handle upvote
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
