package com.example.cookbookapp.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.FavouriteBinding

class Favourite : AppCompatActivity() {

    private lateinit var binding: FavouriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}