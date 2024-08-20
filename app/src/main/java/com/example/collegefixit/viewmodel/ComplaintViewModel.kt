package com.example.collegefixit.viewmodel

import androidx.lifecycle.*
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.repository.ComplaintRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.launch

class ComplaintViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance() // Get Firestore instance
    private val repository = ComplaintRepository(db) // Create repository instance

    private val _complaints = MutableLiveData<List<Complaint>>()
    val complaints: LiveData<List<Complaint>> get() = _complaints

    private var listenerRegistration: ListenerRegistration? = null

    init {
        listenerRegistration = repository.getPendingComplaintsRealtime { updatedComplaints  ->
            _complaints.value = updatedComplaints
        }
    }

    // Function to add a new complaint
    fun addComplaint(complaint: Complaint) = viewModelScope.launch {
        repository.addComplaint(complaint)
    }

    // Function to update the status of a complaint
    fun updateComplaintStatus(complaintId: String, newStatus: String) = viewModelScope.launch {
        repository.updateComplaint(complaintId, newStatus) // Call the correct function
    }

    // Function to upvote a complaint
    fun upvoteComplaint(complaintId: String, userId: String) = viewModelScope.launch {
        repository.upvoteComplaint(complaintId, userId)
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
