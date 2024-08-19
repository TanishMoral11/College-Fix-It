// repository/ComplaintRepository.kt
package com.example.collegefixit.repository

import com.example.collegefixit.model.Complaint
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ComplaintRepository(private val db: FirebaseFirestore) {

    // Function to add a new complaint to Firestore
    suspend fun addComplaint(complaint: Complaint) {
        db.collection("complaints").add(complaint).await()
    }

    // Function to get all pending complaints from Firestore
    suspend fun getPendingComplaints(): List<Complaint> {
        return db.collection("complaints")
            .whereEqualTo("status", "Pending")
            .get()
            .await()
            .toObjects(Complaint::class.java)
    }

    // Function to update the status of a complaint in Firestore
    suspend fun updateComplaint(complaintId: String, newStatus: String) {
        db.collection("complaints").document(complaintId)
            .update("status", newStatus).await()
    }
}
