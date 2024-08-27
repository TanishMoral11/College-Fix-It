package com.example.collegefixit.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.repository.ComplaintRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ComplaintViewModel : ViewModel() {

    private val repository = ComplaintRepository()

    private val _complaints = MutableLiveData<List<Complaint>>()
    val complaints: LiveData<List<Complaint>> get() = _complaints

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        repository.getPendingComplaintsRealtime { updatedComplaints ->
            _complaints.value = updatedComplaints
        }
    }

    fun getComplaintById(id: String): MutableLiveData<Complaint?> {
        val complaintLiveData = MutableLiveData<Complaint?>()
        // Implement the logic to fetch the complaint from your data source
        // For example, if using Firebase:
        FirebaseFirestore.getInstance().collection("complaints")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                val complaint = document.toObject(Complaint::class.java)
                complaintLiveData.value = complaint
            }
            .addOnFailureListener { e ->
                // Handle error
            }
        return complaintLiveData
    }

    fun updateComplaintStatus(complaintId: String, newStatus: String) {
        FirebaseFirestore.getInstance().collection("complaints")
            .document(complaintId)
            .update("status", newStatus)
            .addOnSuccessListener {
                Log.d(TAG, "Complaint status updated successfully")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error updating complaint status", e)
            }
    }
    fun addComplaint(complaint: Complaint) {
        viewModelScope.launch {
            try {
                repository.addComplaint(complaint)
            } catch (e: Exception) {
                // Handle the exception
                _errorMessage.value = "Failed to add complaint"
            }
        }

    }
    fun deleteComplaint(complaintId: String) = viewModelScope.launch {
        try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null && repository.isUserComplaintOwner(complaintId, userId)) {
                repository.deleteComplaint(complaintId)
            } else {
                _errorMessage.value = "You can only delete your own complaints"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Failed to delete complaint: ${e.message}"
        }
    }
    fun toggleUpvote(complaintId: String) = viewModelScope.launch {
        try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                repository.toggleUpvote(complaintId, userId)
            } else {
                _errorMessage.value = "User not logged in"
            }
        } catch (e: Exception) {
            _errorMessage.value = "Failed to toggle upvote: ${e.message}"
        }
    }

    companion object {
        private const val TAG = "ComplaintViewModel"
    }
}
