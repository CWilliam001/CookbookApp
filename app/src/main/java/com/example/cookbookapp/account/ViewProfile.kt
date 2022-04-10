package com.example.cookbookapp.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.CreateAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewProfile : AppCompatActivity() {

    private lateinit var binding: CreateAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        db.collection("User").whereEqualTo("id", intent.getStringExtra("id")).get().addOnSuccessListener {
            //binding.
        }
    }

    private fun checkUser() {

    }
}