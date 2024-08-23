package com.example.collegefixit

import android.animation.Animator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.collegefixit.databinding.ItemComplaintBinding
import com.example.collegefixit.model.Complaint
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
            binding.upvoteIcon.setImageResource(
                if(hasUpvoted) R.drawable.upvotesymbol else R.drawable.normalup
            )

            // Handle upvote button click
            binding.upvoteIcon.setOnClickListener {

                GlobalScope.launch(Dispatchers.Main){
                    onUpvoteClick(complaint.id)

                    val isUpvotedNow = currentUserId in complaint.upvotedBy
                    binding.upvoteIcon.setImageResource(
                        if(isUpvotedNow) R.drawable.upvotesymbol else R.drawable.normalup
                    )

                }
            }

            // Show the delete button only if the current user created the complaint
            if (complaint.userId == currentUserId) {
               binding.deleteIcon.visibility = View.VISIBLE
                binding.deleteIcon.setOnClickListener{
                    binding.upvoteIcon.visibility = View.GONE
                    binding.deleteIcon.visibility = View.GONE
                    binding.deleteAnimationView.visibility = View.VISIBLE
                    binding.deleteAnimationView.playAnimation()

                    binding.deleteAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationEnd(animation: Animator) {
                            onDeleteClick(complaint.id)
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            TODO("Not yet implemented")
                        }

                        override fun onAnimationRepeat(animation: Animator) {
                            TODO("Not yet implemented")
                        }

                    })
                }
            } else {
                binding.deleteIcon.visibility = View.GONE
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
