package com.example.cookbookapp.account

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.cookbookapp.MainActivity
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.EditProfileBinding
import com.example.cookbookapp.model.Users
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import java.util.*

class EditProfile : AppCompatActivity() {

    private lateinit var binding: EditProfileBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private var firstName = ""
    private var lastName = ""
    private var email = ""
    private var oldPassword = ""
    private var password = ""
    private var newPassword = ""
    private var status = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
        val sharedUid = sharedPreferences.getString("StringUid", null)
        db = FirebaseFirestore.getInstance()
        firebaseAuth  = FirebaseAuth.getInstance()

        var registerDate = Date()
        var stringDate = ""

        db.collection("User").whereEqualTo("id", sharedUid).get().addOnSuccessListener {
            for (document in it) {
                binding.editTextEditAccFirstName.setText(document.get("firstName").toString())
                binding.editTextEditAccLastName.setText(document.get("lastName").toString())
                binding.editTextEditAccEmail.setText(document.get("email").toString())
                oldPassword = document.get("password").toString()
                status = document.get("status").toString()
            }
        }



        binding.buttonBackToViewProfile.setOnClickListener{
            startActivity(Intent(this, ViewProfile::class.java))
            finish()
        }

        binding.buttonSaveProfileChanges.setOnClickListener{
            firstName = binding.editTextEditAccFirstName.text.toString().trim()
            lastName = binding.editTextEditAccLastName.text.toString().trim()
            email = binding.editTextEditAccEmail.text.toString().trim()
            password = binding.editTextEditAccOldPassword.text.toString().trim()
            newPassword = binding.editTextEditAccNewPassword.text.toString().trim()

            if (TextUtils.isEmpty(firstName)) {
                binding.editTextEditAccFirstName.error = "Please enter your first name"
            } else if (TextUtils.isEmpty(lastName)) {
                binding.editTextEditAccLastName.error = "Please enter your last name"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.editTextEditAccEmail.error = "Invalid email format"
            } else {
                // All data is valid
                // Check if the old password filed got any changes
                var passwordChanges = checkPasswordChanges()
                var pw = if (passwordChanges) password else oldPassword
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser != null) {
                    val credential = EmailAuthProvider.getCredential(email, pw)

                    // Change email
                    firebaseUser.reauthenticate(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            if (passwordChanges) {
                                // Change password
                                firebaseUser!!.updatePassword(newPassword)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            var user = Users(sharedUid.toString(), email, firstName, lastName, pw, registerDate, status)
                                            db.collection("User").document("$sharedUid").set(user)
                                            Toast.makeText(this, "User data has been modified", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                var user = Users(sharedUid.toString(), email, firstName, lastName, pw, registerDate, status)
                                db.collection("User").document("$sharedUid").set(user)
                                Toast.makeText(this, "User data has been modified", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun checkPasswordChanges() : Boolean {
        password = binding.editTextEditAccOldPassword.text.toString().trim()
        if (password == null){
            // Theres no changes in old password field
            return false
        } else {
            // Theres a changes in old password field
            if (oldPassword != password) {
                binding.editTextEditAccOldPassword.error = "The old password is not correct"
            } else if (TextUtils.isEmpty(newPassword)) {
                binding.editTextEditAccNewPassword.error = "Please enter new password"
            } else if (newPassword.length < 8) {
                binding.editTextEditAccNewPassword.error = "Password length must at least 8 characters long"
            }
        }
        return true
    }
}