package com.example.cookbookapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.account.CreateAccount
import com.example.cookbookapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.textViewCreateNewAccount.setOnClickListener{
            intent = Intent(this, CreateAccount::class.java)
        }

        binding.buttonLogin.setOnClickListener{
            // Call db to check auth
        }
    }
}