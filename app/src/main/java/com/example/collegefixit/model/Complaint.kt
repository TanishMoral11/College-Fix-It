package com.example.collegefixit.model

data class Complaint(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val status: String = "Pending",
    var upvotes: Int = 0,
    var upvotedBy: MutableList<String> = mutableListOf()
)