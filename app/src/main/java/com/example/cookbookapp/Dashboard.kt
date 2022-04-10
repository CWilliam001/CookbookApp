package com.example.cookbookapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cookbookapp.account.Favourite
import com.example.cookbookapp.account.History
import com.example.cookbookapp.account.ViewProfile
import com.example.cookbookapp.databinding.DashboardBinding
import com.example.cookbookapp.model.Tag
import com.example.cookbookapp.model.TagList
import com.example.cookbookapp.recipe.CreateRecipe
import com.example.cookbookapp.recipe.SearchRecipe
import com.google.firebase.auth.FirebaseAuth

class Dashboard : AppCompatActivity() {

    private lateinit var binding: DashboardBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
        val sharedUid = sharedPreferences.getString("StringUid", null)
        binding.textViewDashboardId.text = "UID : $sharedUid"

        // Binding buttons
        binding.buttonToSearch.setOnClickListener{
            intent = Intent(this, SearchRecipe::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonToAddRecipe.setOnClickListener{
            intent = Intent(this, CreateRecipe::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonToViewProfile.setOnClickListener{
            intent = Intent(this, ViewProfile::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonToMyRecipe.setOnClickListener{
            intent = Intent(this, MyRecipe::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonToTags.setOnClickListener{
            intent = Intent(this, TagList::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonToFavourites.setOnClickListener{
            intent = Intent(this, Favourite::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonToHistory.setOnClickListener{
            intent = Intent(this, History::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonToLogout.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null) {
            // Nothing
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}