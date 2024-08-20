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


    // Function to increment or decrement the upvotes of a complaint
    // In ComplaintRepository.kt
    suspend fun upvoteComplaint(complaintId: String, userId: String) {
        try {
            val complaintRef = db.collection("complaints").document(complaintId)
            db.runTransaction { transaction ->
                val snapshot = transaction.get(complaintRef)
                val currentUpvotes = snapshot.get("upvotes") as? List<String> ?: emptyList()

                // Check if the user has already upvoted
                val updatedUpvotes = if (currentUpvotes.contains(userId)) {
                    // User has already upvoted, remove the upvote
                    currentUpvotes.filter { it != userId }
                } else {
                    // User has not upvoted, add the upvote
                    currentUpvotes + userId
                }

                transaction.update(complaintRef, "upvotes", updatedUpvotes)
            }.await()
        } catch (e: Exception) {
            // Handle exception
        }
    }

}
