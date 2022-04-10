package com.example.cookbookapp.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.cookbookapp.databinding.CreateAccountBinding

class CreateAccount : AppCompatActivity() {

    private lateinit var binding : CreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var editText_loginEmail = findViewById(binding.editText_loginEmail) as EditText
        var editText_loginPassword = findViewById(binding.editText_loginPassword) as EditText
        var btn_create = findViewById(binding.id.btn_reset) as Button
        var btn_login = findViewById(binding.id.button_login) as Button

        // set on-click listener
        button_login.setOnClickListener {
            val email = editText_loginEmail.text;
            val password = editText_loginPassword.text;
            Toast.makeText(this@CreateAccount, email, Toast.LENGTH_LONG).show()
    }
}