// viewmodel/ComplaintViewModel.kt
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
        loadPendingComplaints() // Load pending complaints on ViewModel initialization
    }

    // Function to load pending complaints
    private fun loadPendingComplaints() = viewModelScope.launch {
        _complaints.value = repository.getPendingComplaints()
    }

    // Function to add a new complaint
    fun addComplaint(complaint: Complaint) = viewModelScope.launch {
        repository.addComplaint(complaint)
        loadPendingComplaints() // Reload complaints after adding a new one
    }

    // Function to update the status of a complaint
    fun updateComplaint(complaintId: String, newStatus: String) = viewModelScope.launch {
        repository.updateComplaint(complaintId, newStatus)
        loadPendingComplaints() // Reload complaints after updating
    }
}
