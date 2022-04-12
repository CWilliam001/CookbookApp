package com.example.cookbookapp.recipe

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.ViewRecipeBinding
import com.example.cookbookapp.model.Tag
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ViewRecipe : AppCompatActivity() {

    private lateinit var binding: ViewRecipeBinding
    private lateinit var db : FirebaseFirestore

    private var recipeId = ""
//    private var firstName = ""
//    private var lastName = ""
    private var uid = ""
    private var photo = ""
    private var link = ""
    private var tagList = ArrayList<Int>()
    private var tagObjList = ArrayList<Tag>()
    private var tagContent = ArrayList<String>()
//    private var tagString = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ViewRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
        val sharedUid = sharedPreferences.getString("StringUid", null)

        db = FirebaseFirestore.getInstance()

        recipeId = intent.getStringExtra("id").toString()

        val query: Query = db.collection("Recipe")

        // Get Recipe Data
        query.whereEqualTo("id", recipeId).get().addOnSuccessListener {
            for (document in it) {
                binding.textViewViewRecipeName.text = document.get("name").toString()

                binding.textViewDescriptionContent.text = document.get("description").toString()
                binding.textViewNotesContent.text = document.get("notes").toString()
                binding.textViewIngredientsContent.text = document.get("ingredients").toString()
                binding.textViewExtraInformationContent.text = document.get("extraInformation").toString()
                link = document.get("link").toString()
                if(document.get("link").toString() == "") {
                    binding.textViewViewLink.visibility = View.GONE
                    binding.textViewLinkContent.visibility = View.GONE
                } else {
                    binding.textViewLinkContent.text = document.get("link").toString()
                }
                uid = document.get("userID").toString()
                binding.textViewUserName.text = "By User ID: $uid"
                tagList = document.get("tagList") as ArrayList<Int>
                if(tagList.isEmpty()) {
                    binding.textViewTags.visibility = View.GONE
                    binding.textViewTagsContent.visibility = View.GONE
                } else {
                    binding.textViewTagsContent.text = tagList.toString()
                }
                if (uid == sharedUid) {
                    // Display the button if the recipe owner has visit this page
                    binding.buttonToEditRecipe.visibility = View.VISIBLE
                }

                // Get image from Storage
                photo = document.get("photo").toString()
                val storageRef = FirebaseStorage.getInstance().reference.child("images/$photo")
                val localFile = File.createTempFile("temp", "jpg")
                storageRef.getFile(localFile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    binding.imageViewViewRecipePhoto.visibility = View.VISIBLE
                    binding.imageViewViewRecipePhoto.setImageBitmap(bitmap)
                    Log.e("Error 2", it.toString())

                }
                    .addOnFailureListener{
                        Log.e("Error", it.toString())
                        Toast.makeText(this, "Error : ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
            .addOnFailureListener{
                e ->
                Log.e("Firestore Error: ", e.message.toString())
            }

        binding.textViewLinkContent.setOnClickListener{
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("$link"))
            startActivity(intent)
        }

        binding.buttonToEditRecipe.setOnClickListener{
            if (uid == sharedUid) {
                val intent = Intent(this, EditRecipe::class.java)
                intent.putExtra("id", recipeId)
                startActivity(intent)
            }
        }
    }

}