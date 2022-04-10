package com.example.cookbookapp.account

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.MainActivity
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.CreateAccountBinding
import com.example.cookbookapp.databinding.ViewProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewProfile : AppCompatActivity() {

    private lateinit var binding: ViewProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
        val sharedUid = sharedPreferences.getString("StringUid", null)

        val uid = checkUser()
        db.collection("User").whereEqualTo("id", sharedUid).get().addOnSuccessListener {
            for (document in it) {
                binding.textViewViewProfileId.text = "UID : " + sharedUid
                binding.textViewViewProfileName.text = "Name : " + document.get("firstName").toString() + " " + document.get("lastName").toString()
                binding.textViewViewProfileEmail.text = "Email : " + document.get("email").toString()
            }
        }

        binding.buttonBackToDashboard.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        binding.buttonToEditProfile.setOnClickListener{
            startActivity(Intent(this, EditProfile::class.java))
            finish()
        }

        binding.buttonDeleteProfile.setOnClickListener{
            // Need to redirect to new activity or display a dialog box to user to press yes no
        }

    }

    private fun checkUser() : String {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null) {
            // Do nothing
            return firebaseUser!!.uid
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        return ""
    }
}