package com.example.cookbookapp

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.databinding.DashboardBinding

class Dashboard : AppCompatActivity() {

    private lateinit var binding: DashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Binding buttons
        binding.buttonToSearch.setOnClickListener{
            // intent = Intent(this, AddRecipe::class.java)
            // startActivity(intent)
            // finish()
        }

        binding.buttonToAddRecipe.setOnClickListener{
            // intent = Intent(this, AddRecipe::class.java)
            // startActivity(intent)
            // finish()
        }

        binding.buttonToMyRecipe.setOnClickListener{
            // intent = Intent(this, AddRecipe::class.java)
            // startActivity(intent)
            // finish()
        }

        binding.buttonToTags.setOnClickListener{
            // intent = Intent(this, AddRecipe::class.java)
            // startActivity(intent)
            // finish()
        }

        binding.buttonToFavourites.setOnClickListener{
            // intent = Intent(this, AddRecipe::class.java)
            // startActivity(intent)
            // finish()
        }

        binding.buttonToHistory.setOnClickListener{
            // intent = Intent(this, AddRecipe::class.java)
            // startActivity(intent)
            // finish()
        }
    }
}