package com.example.collegefixit.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.collegefixit.model.Complaint
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class ComplaintRepository {

    private val db = FirebaseFirestore.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val complaintsCollection = db.collection("complaints")

    // Function to get complaints in real-time
    fun getPendingComplaintsRealtime(onUpdate: (List<Complaint>) -> Unit): ListenerRegistration {
        return complaintsCollection
            .whereEqualTo("status", "Pending")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                val complaints = snapshot?.documents?.mapNotNull { it.toObject(Complaint::class.java) } ?: emptyList()
                onUpdate(complaints)
            }
    }

    // Function to upvote a complaint
    suspend fun upvoteComplaint(complaintId: String) {
        try {
            val complaintRef = firestore.collection("complaints").document(complaintId)
            firestore.runTransaction { transaction ->
                val complaintSnapshot = transaction.get(complaintRef)
                val currentUpvoteCount = complaintSnapshot.getLong("upvoteCount") ?: 0
                transaction.update(complaintRef, "upvoteCount", currentUpvoteCount + 1)
            }.await()
        } catch (e: Exception) {
            // Handle the error
            e.printStackTrace()
        }
    }

    // Function to add a new complaint
    suspend fun addComplaint(complaint: Complaint) {
        complaintsCollection.add(complaint).await()
    }
}
