package com.example.cookbookapp.account

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.MainActivity
import com.example.cookbookapp.databinding.CreateAccountBinding
import com.example.cookbookapp.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import java.text.SimpleDateFormat
import java.util.*

class CreateAccount : AppCompatActivity() {

    private lateinit var binding : CreateAccountBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private var firstName = ""
    private var lastName = ""
    private var email = ""
    private var password = ""
    // private var rePassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonBackToLogin.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.buttonCreateAccount.setOnClickListener{
            validateData()
        }
    }

    private fun validateData() {
        firstName = binding.editTextCreateAccFirstName.text.toString().trim()
        lastName = binding.editTextCreateAccLastName.text.toString().trim()
        email = binding.editTextCreateAccEmail.text.toString().trim()
        password = binding.editTextCreateAccPassword.text.toString().trim()
        // rePassword = binding.editTextcreateAccReEnterPassword.text.toString().trim()

        if (TextUtils.isEmpty(firstName)) {
            binding.editTextCreateAccFirstName.error = "Please enter your first name"
        } else if (TextUtils.isEmpty(firstName)) {
            binding.editTextCreateAccLastName.error = "Please enter your last name"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.editTextCreateAccEmail.error = "Invalid email format"
        } else if (TextUtils.isEmpty(password)) {
            binding.editTextCreateAccPassword.error = "Please enter password"
        } else if (password.length < 8) {
            binding.editTextCreateAccPassword.error = "Password length must at least 8 characters long"
        } /* else if (TextUtils.isEmpty(rePassword)) {
            binding.editTextcreateAccReEnterPassword.error = "Please re-enter password"
        } else if (password != rePassword) {
            binding.editTextcreateAccReEnterPassword.error = "The password you re-enter is not the same"
        } */ else {
            firebaseSignUp()
        }

    }

    private fun firebaseSignUp() {
        firstName = binding.editTextCreateAccFirstName.text.toString().trim()
        lastName = binding.editTextCreateAccLastName.text.toString().trim()
        email = binding.editTextCreateAccEmail.text.toString().trim()
        password = binding.editTextCreateAccPassword.text.toString().trim()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            db = FirebaseFirestore.getInstance()

            //Get user email and ID
            val firebaseUser = firebaseAuth.currentUser
            val id = firebaseUser!!.uid
            val email = firebaseUser!!.email.toString()
            val now = Date()

            val user = Users(id, email, firstName, lastName, password, now, "activate")
            db.collection("User").document("$id").set(user)

            // Redirect to dashboard
            startActivity(Intent(this, Dashboard::class.java))
            val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.apply{
                putString("StringUid", id)
            }.apply()
            finish()
            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener{
                e ->
                Toast.makeText(this, "Sign up failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}