package com.example.collegefixit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.collegefixit.databinding.ItemComplaintBinding
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.viewmodel.ComplaintViewModel

class ComplaintsAdapter(private val viewModel: ComplaintViewModel) :
    ListAdapter<Complaint, ComplaintsAdapter.ComplaintViewHolder>(ComplaintDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val binding = ItemComplaintBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ComplaintViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = getItem(position)
        holder.bind(complaint)
    }

    inner class ComplaintViewHolder(private val binding: ItemComplaintBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(complaint: Complaint) {
            binding.titleTextView.text = complaint.title
            binding.descriptionTextView.text = complaint.description
            binding.statusTextView.text = complaint.status
            binding.upvoteCountTextView.text = "${complaint.upvotes} Upvotes"

            binding.upvoteButton.setOnClickListener {
                viewModel.upvoteComplaint(complaint.id)
            }
        }
    }

    class ComplaintDiffCallback : DiffUtil.ItemCallback<Complaint>() {
        override fun areItemsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Complaint, newItem: Complaint): Boolean {
            return oldItem == newItem
        }
    }
}
