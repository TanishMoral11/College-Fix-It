package com.example.collegefixit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.example.collegefixit.model.Complaint
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await

class ComplaintRepository {

    private val db = FirebaseFirestore.getInstance()
    private val complaintsCollection = db.collection("complaints")

    suspend fun isUserComplaintOwner(complaintId: String, userId: String): Boolean {
        return try {
            val complaintDoc = complaintsCollection.document(complaintId).get().await()
            complaintDoc.getString("userId") == userId
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteComplaint(complaintId: String) {
        try {
            complaintsCollection.document(complaintId).delete().await()
        } catch (e: Exception) {
            throw e
        }
    }

    fun getPendingComplaintsRealtime(onUpdate: (List<Complaint>) -> Unit): ListenerRegistration {
        return complaintsCollection
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }
                val complaints = snapshot?.documents?.mapNotNull {
                    it.toObject(Complaint::class.java)?.copy(id = it.id)
                } ?: emptyList()
                onUpdate(complaints)
            }
    }

    suspend fun toggleUpvote(complaintId: String, userId: String) {
        try {
            val complaintRef = complaintsCollection.document(complaintId)
            db.runTransaction { transaction ->
                val complaintSnapshot = transaction.get(complaintRef)
                val currentUpvotes = complaintSnapshot.getLong("upvotes") ?: 0
                val upvotedBy = complaintSnapshot.get("upvotedBy") as? MutableList<String> ?: mutableListOf()

                if (userId in upvotedBy) {
                    transaction.update(complaintRef, "upvotes", currentUpvotes - 1)
                    upvotedBy.remove(userId)
                } else {
                    transaction.update(complaintRef, "upvotes", currentUpvotes + 1)
                    upvotedBy.add(userId)
                }
                transaction.update(complaintRef, "upvotedBy", upvotedBy)
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

    fun getComplaintByIdLive(id: String): LiveData<Complaint?> {
        val complaintLiveData = MutableLiveData<Complaint?>()
        complaintsCollection.document(id).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                complaintLiveData.value = null
                return@addSnapshotListener
            }
            complaintLiveData.value = snapshot?.toObject(Complaint::class.java)?.copy(id = snapshot.id)
        }
        return complaintLiveData
    }

    suspend fun updateComplaintStatus(complaintId: String, newStatus: String) {
        try {
            complaintsCollection.document(complaintId).update("status", newStatus).await()
        } catch (e: Exception) {
            throw e
        }
    }
}