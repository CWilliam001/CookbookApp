package com.example.cookbookapp.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.CreateRecipeBinding

class CreateRecipe : AppCompatActivity() {

    private lateinit var binding: CreateRecipeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}