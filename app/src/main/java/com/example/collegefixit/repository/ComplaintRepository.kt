package com.example.collegefixit.repository

import com.example.collegefixit.model.Complaint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.tasks.await

class ComplaintRepository(private val db : FirebaseFirestore) {

    //Function to add a new complaint to FireStore
    suspend fun addComplaint(complaint : Complaint){
        db.collection("complaints").add(complaint).await()

    }
        suspend fun getPendingComplaints(): List<Complaint> {
            return db.collection("complaints")
            .whereEqualTo("status", "Pending")
            .get()
            .await()
            .toObjects(Complaint::class.java)
    }

    //Function to update the status of a complaint in FireStore
    suspend fun updateComplaint(complaintId : String, newString: String){
        db.collection("complaints").document(complaintId)
            .update("status", newString)
            .await()
    }
}