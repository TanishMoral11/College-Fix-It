package com.example.collegefixit.model

data class Complaint(
    val id : Int,
    val title : String,
    val description : String,
    val status : String,
    val upvotes: Int = 0
)
