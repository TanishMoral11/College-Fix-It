package com.example.collegefixit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.collegefixit.R
import com.example.collegefixit.model.Complaint

class GuardComplaintsAdapter(
    private val onDetailsClick: (Complaint) -> Unit
) : ListAdapter<Complaint, GuardComplaintsAdapter.ComplaintViewHolder>(ComplaintDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComplaintViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_guard_complaint, parent, false)
        return ComplaintViewHolder(view)
    }

    override fun onBindViewHolder(holder: ComplaintViewHolder, position: Int) {
        val complaint = getItem(position)
        holder.bind(complaint)
    }

    inner class ComplaintViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val upvoteCountTextView: TextView = itemView.findViewById(R.id.upvoteCount)
        private val moreDetailsButton: View = itemView.findViewById(R.id.moreDetailsIcon)
        private val statusIcon: ImageView = itemView.findViewById(R.id.statusIcon)

        fun bind(complaint: Complaint) {
            titleTextView.text = complaint.title
            descriptionTextView.text = complaint.description
            upvoteCountTextView.text = complaint.upvotes.toString()

            when (complaint.status) {
                "Pending" -> statusIcon.setImageResource(R.drawable.red_dot)
                "On Hold" -> statusIcon.setImageResource(R.drawable.pending)
                "Solved" -> statusIcon.setImageResource(R.drawable.greentick)
            }

            moreDetailsButton.setOnClickListener {
                onDetailsClick(complaint)
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
