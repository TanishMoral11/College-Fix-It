package com.example.collegefixit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.repository.ComplaintRepository
import com.google.firebase.auth.FirebaseAuth
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

    fun addComplaint(complaint: Complaint) = viewModelScope.launch {
        try{
            repository.addComplaint(complaint)
        } catch (e: Exception) {
            _errorMessage.value = "Failed to add complaint: ${e.message}"
        }
    }
}
