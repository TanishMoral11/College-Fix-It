package com.example.collegefixit.model

data class Complaint(
    val id: String = "", // Unique ID for each complaint
    val title: String = "",
    val description: String = "",
    val status: String = "Pending", // Default status is "Pending"
    val upvotes: Int = 0 // Number of upvotes
)
