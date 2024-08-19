package com.example.collegefixit.repository

import com.example.collegefixit.model.Complaint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class ComplaintRepository(private val db: FirebaseFirestore) {

    // Function to add a new complaint to Firestore
    suspend fun addComplaint(complaint: Complaint) {
        db.collection("complaints").add(complaint).await()
    }

    // Function to retrieve all pending complaints from Firestore with real-time updates
    fun getPendingComplaintsRealtime(onDataChange: (List<Complaint>) -> Unit): ListenerRegistration {
        return db.collection("complaints")
            .whereEqualTo("status", "Pending")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    onDataChange(emptyList())
                    return@addSnapshotListener
                }
                val complaints = snapshot.toObjects(Complaint::class.java)
                onDataChange(complaints)
            }
    }

    // Function to update the status of a complaint
    suspend fun updateComplaint(complaintId: String, newStatus: String) {
        val complaintRef = db.collection("complaints").document(complaintId)
        complaintRef.update("status", newStatus).await()
    }

    // Function to increment the upvotes of a complaint
    suspend fun upvoteComplaint(complaintId: String) {
        try {
            val complaintRef = db.collection("complaints").document(complaintId)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(complaintRef)
                val newUpvotes = snapshot.getLong("upvotes")?.plus(1) ?: 1
                transaction.update(complaintRef, "upvotes", newUpvotes)
            }.await()
        } catch (e: Exception) {
            // Handle exception
        }
    }
}
