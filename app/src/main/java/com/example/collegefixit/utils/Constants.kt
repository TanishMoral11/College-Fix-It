package com.example.collegefixit.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Constants {
    companion object{
        val auth= FirebaseAuth.getInstance()
        val fireStoreReference=FirebaseFirestore.getInstance()
    }
}