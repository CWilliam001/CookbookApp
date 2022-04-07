package com.example.cookbookapp.recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.SearchRecipeBinding

class SearchRecipe : AppCompatActivity() {

    private lateinit var binding: SearchRecipeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}