package com.example.collegefixit.viewmodel

import androidx.lifecycle.*
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.repository.ComplaintRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ComplaintViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance() // Get Firestore instance
    private val repository = ComplaintRepository(db) // Create repository instance

    private val _complaints = MutableLiveData<List<Complaint>>()
    val complaints: LiveData<List<Complaint>> get() = _complaints

    init {
        observePendingComplaints() // Observe real-time updates
    }

    // Function to observe pending complaints in real-time
    private fun observePendingComplaints() {
        repository.getPendingComplaintsRealtime { complaints ->
            _complaints.value = complaints
        }
    }

    // Function to add a new complaint
    fun addComplaint(complaint: Complaint) = viewModelScope.launch {
        repository.addComplaint(complaint)
    }

    // Function to update the status of a complaint
    fun updateComplaint(complaintId: String, newStatus: String) = viewModelScope.launch {
        repository.updateComplaint(complaintId, newStatus)
    }

    // Function to upvote a complaint
    fun upvoteComplaint(complaintId: String) = viewModelScope.launch {
        repository.upvoteComplaint(complaintId)
    }
}
