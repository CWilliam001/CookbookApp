package com.example.cookbookapp.recipe

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.ViewRecipeBinding
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore

class ViewRecipe : AppCompatActivity() {

    private lateinit var binding: ViewRecipeBinding
    private lateinit var db : FirebaseFirestore

    private var name = ""
    private var uid = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
        val sharedUid = sharedPreferences.getString("StringUid", null)

        db = FirebaseFirestore.getInstance()

        db.collection("Recipe").whereEqualTo("name", intent.getStringExtra("name")).get().addOnSuccessListener {
            for (document in it) {
                binding.textViewViewRecipeName.text = document.get("firstName").toString()
                name = document.get("firstName").toString()
                uid = document.get("userId").toString()
                binding.imageViewViewRecipePhoto.setImageURI(Uri.parse(document.get("photo").toString()))
                binding.textViewDescriptionContent.text = document.get("descrription").toString()
                binding.textViewNotesContent.text = document.get("notes").toString()
                binding.textViewIngredientsContent.text = document.get("ingredients").toString()
                binding.textViewExtraInformationContent.text = document.get("extraInformation").toString()
                binding.textViewLinkContent.text = document.get("link").toString()
            }
        }
            .addOnFailureListener{
                e ->
                Log.e("Firestore Error: ", e.message.toString())
            }

        db.collection("User").whereEqualTo("id", uid).get().addOnSuccessListener {
            for (document in it) {
                binding.textViewUserName.text = document.get("firstName").toString() + document.get("lastName").toString()
            }
        }

        binding.buttonToEditRecipe.setOnClickListener{
            val intent = Intent(this, EditRecipe::class.java)
            intent.putExtra("name", name)
            startActivity(intent)

        }
    }
}