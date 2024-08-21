package com.example.collegefixit.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.example.collegefixit.model.Complaint
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class ComplaintRepository {

    private val db = FirebaseFirestore.getInstance()
    private val complaintsCollection = db.collection("complaints")

    fun getPendingComplaintsRealtime(onUpdate: (List<Complaint>) -> Unit): ListenerRegistration {
        return complaintsCollection
            .whereEqualTo("status", "Pending")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // Handle error
                    return@addSnapshotListener
                }
                val complaints = snapshot?.documents?.mapNotNull {
                    it.toObject(Complaint::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onUpdate(complaints)
            }
    }

    suspend fun upvoteComplaint(complaintId: String) {
        try {
            val complaintRef = complaintsCollection.document(complaintId)
            db.runTransaction { transaction ->
                val complaintSnapshot = transaction.get(complaintRef)
                val currentUpvoteCount = complaintSnapshot.getLong("upvotes") ?: 0
                transaction.update(complaintRef, "upvotes", currentUpvoteCount + 1)
            }.await()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun addComplaint(complaint: Complaint) {
        try {
            complaintsCollection.add(complaint).await()
        } catch (e: Exception) {
            throw e
        }
    }
}