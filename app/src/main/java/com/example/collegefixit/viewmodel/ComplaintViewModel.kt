package com.example.collegefixit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegefixit.model.Complaint
import com.example.collegefixit.repository.ComplaintRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

//The ViewModel will communicate with the repository and provide data to the UI.
class ComplaintViewModel:ViewModel() {
    //this is used to communicate with the FireStore database
    //the use of instance is
    private val db = FirebaseFirestore.getInstance()
    private val repository = ComplaintRepository(db)

    private val _complaints = MutableLiveData<List<Complaint>>() //List of complaints
    val complaints : LiveData<List<Complaint>> get() = _complaints

    init{
        loadPendingComplaints() // Load pending complaints on ViewModel initialization
    }

    // Function to load pending complaints
    private fun loadPendingComplaints() = viewModelScope.launch {
        _complaints.value = repository.getPendingComplaints()
    }

    fun addComplaint(complaint: Complaint) = viewModelScope.launch {
        repository.addComplaint(complaint)
        loadPendingComplaints() // Reload complaints after adding a new one
    }

    fun updateComplaint(complaintId : String, newStatus : String) = viewModelScope.launch {
        repository.updateComplaint(complaintId, newStatus)
        loadPendingComplaints() // Reload complaints after updating
    }

}