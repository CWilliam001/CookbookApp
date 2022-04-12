package com.example.cookbookapp.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.DeleteAccountBinding
import com.example.cookbookapp.databinding.DeleteRecipeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DeleteRecipe : AppCompatActivity() {

    private lateinit var binding: DeleteRecipeBinding
    private lateinit var db: FirebaseFirestore

    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DeleteRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        binding.buttonDeleteRecipeNo.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
            Toast.makeText(this, "Delete recipe cancelled", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.buttonDeleteRecipeYes.setOnClickListener{
            db.collection("Recipe").document(intent.getStringExtra("id").toString()).delete().addOnSuccessListener {
                startActivity(Intent(this, Dashboard::class.java))
                finish()
            }
                .addOnFailureListener{
                    Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, Dashboard::class.java))
                    finish()
                }
        }
    }
}