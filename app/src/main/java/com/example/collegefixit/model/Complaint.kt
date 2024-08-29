package com.example.collegefixit.model

import com.google.firebase.Timestamp

data class Complaint(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    var status: String = "Pending",
    var upvotes: Int = 0,
    var upvotedBy: MutableList<String> = mutableListOf(),
    val userId: String = "" , // ID of the user who created the complaint
    val timestamp: Long = System.currentTimeMillis()
)
