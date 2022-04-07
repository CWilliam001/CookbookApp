package com.example.cookbookapp.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.HistoryBinding

class History : AppCompatActivity() {

    private lateinit var binding: HistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}