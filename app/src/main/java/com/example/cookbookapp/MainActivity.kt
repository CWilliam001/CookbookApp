package com.example.cookbookapp

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.cookbookapp.account.CreateAccount
import com.example.cookbookapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init Firestore
        db = FirebaseFirestore.getInstance()

        // Init FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        binding.textViewCreateNewAccount.setOnClickListener{
            intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonLogin.setOnClickListener{
            // Call db to check auth
            validateData()
            // If user is found redirect them to dashboard & set session

            // Else display error message
        }
    }

    private fun validateData() {
        email = binding.editTextLoginEmail.text.toString().trim()
        password = binding.editTextLoginPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextLoginEmail.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            binding.editTextLoginPassword.error = "Please enter password"
        } else {
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            val firebaseUser = firebaseAuth.currentUser
            val uid = firebaseUser!!.uid
            Toast.makeText(this, "UID: ${uid}, login successful", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Dashboard::class.java))
            val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply{
                putString("StringUid", uid)
            }.apply()
            finish()
        }
            .addOnFailureListener{
                e ->
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            // If session still exist then dun allow the user come to login page
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }
    }
}