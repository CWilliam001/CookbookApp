package com.example.cookbookapp.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.databinding.CreateAccountBinding

class CreateAccount : AppCompatActivity() {

    private lateinit var binding : CreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}