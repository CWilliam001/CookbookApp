package com.example.cookbookapp.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.ViewRecipeBinding

class ViewRecipe : AppCompatActivity() {

    private lateinit var binding: ViewRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}