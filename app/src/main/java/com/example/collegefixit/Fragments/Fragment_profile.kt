package com.example.collegefixit.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.collegefixit.Auth.LoginActivity
import com.example.collegefixit.databinding.FragmentProfileBinding
import com.example.collegefixit.viewmodel.ComplaintViewModel
import com.google.firebase.auth.FirebaseAuth

class fragment_profile : Fragment() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding : FragmentProfileBinding
    private lateinit var viewModel : ComplaintViewModel



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.logoutButton.setOnClickListener {
            auth.signOut()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }



}