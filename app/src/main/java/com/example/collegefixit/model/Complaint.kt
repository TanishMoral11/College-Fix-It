package com.example.collegefixit.model

import com.google.firebase.Timestamp

data class Complaint(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    var status: String = "Pending",
    var upvotes: Int = 0,
    var upvotedBy: MutableList<String> = mutableListOf(),
    val userId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    var imageUrl: String? = null
)