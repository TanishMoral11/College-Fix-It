package com.example.collegefixit.model

data class Complaint(
    val id: String = "", // Firebase auto-generated ID
    val title: String = "",
    val description: String = "",
    val status: String = "Pending", // Default status is pending
    var upvotes: Int = 0 // Number of upvotes
)
