package com.example.cookbookapp.account

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.MainActivity
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.DeleteAccountBinding
import com.example.cookbookapp.model.Users
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.Date.*

class DeleteAccount : AppCompatActivity() {

    private lateinit var binding: DeleteAccountBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    private var firstName = ""
    private var lastName = ""
    private var email = ""
    private var registerDate = Date()
    private var password = ""
    private var status = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DeleteAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
        val sharedUid = sharedPreferences.getString("StringUid", null)

        binding.buttonDeleteAccountNo.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
            Toast.makeText(this, "Delete account cancelled", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.buttonDeleteAccountYes.setOnClickListener{
            db.collection("User").whereEqualTo("id", sharedUid).get().addOnSuccessListener {
                for (document in it) {
                    firstName = document.get("firstName").toString()
                    lastName = document.get("lastName").toString()
                    email = document.get("email").toString()
                    password = document.get("password").toString()
                    status = document.get("status").toString()
                }
            }

            val user = Users(sharedUid.toString(), email, firstName, lastName, password, registerDate, "deactivate" )
            db.collection("User").document("$sharedUid").set(user)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}