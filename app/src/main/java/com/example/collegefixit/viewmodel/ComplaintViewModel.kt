package com.example.collegefixit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.repository.ComplaintRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ComplaintViewModel : ViewModel() {

    private val repository = ComplaintRepository()

    private val _complaints = MutableLiveData<List<Complaint>>()
    val complaints: LiveData<List<Complaint>> get() = _complaints

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    init {
        repository.getPendingComplaintsRealtime { updatedComplaints ->
            _complaints.value = sortComplaints(updatedComplaints)
        }
    }

    private fun sortComplaints(complaints: List<Complaint>): List<Complaint> {
        return complaints.sortedWith(
            compareBy<Complaint> { complaints ->
                when (complaints.status) {
                    "On Hold" -> 0
                    "Pending" -> 1
                    "Solved" -> 2
                    else -> 3
                }
            }
                .thenByDescending { it.upvotes }
                .thenBy { it.timestamp }
        )
    }

    fun getComplaintById(id: String): LiveData<Complaint?> {
        return repository.getComplaintByIdLive(id)
    }

    fun updateComplaintStatus(complaintId: String, newStatus: String) {
        viewModelScope.launch {
            try {
                repository.updateComplaintStatus(complaintId, newStatus)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update complaint status: ${e.message}"
            }
        }
    }

    fun addComplaint(complaint: Complaint) {
        viewModelScope.launch {
            try {
                repository.addComplaint(complaint)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add complaint: ${e.message}"
            }
        }
    }

    fun deleteComplaint(complaintId: String) {
        viewModelScope.launch {
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
    }

    fun toggleUpvote(complaintId: String) {
        viewModelScope.launch {
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
    }

    companion object {
        private const val TAG = "ComplaintViewModel"
    }
}
