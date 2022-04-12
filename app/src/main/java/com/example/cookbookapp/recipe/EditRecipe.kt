package com.example.cookbookapp.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.EditRecipeBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditRecipe : AppCompatActivity() {

    private lateinit var binding: EditRecipeBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}