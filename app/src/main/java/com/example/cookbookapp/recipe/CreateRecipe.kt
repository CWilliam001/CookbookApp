package com.example.cookbookapp.recipe

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.se.omapi.Session
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.CreateRecipeBinding
import com.example.cookbookapp.model.Tag
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateRecipe : AppCompatActivity() {

    private lateinit var binding: CreateRecipeBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var recipe_photo : ImageView
    private lateinit var ImageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var listViewTags = binding.listViewTags
        var tagDataModel = ArrayList<Tag>()

        db = FirebaseFirestore.getInstance()
        db.collection("Tag").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("Firebase error", error.message.toString())
                    return
                }

                for (dc : DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        tagDataModel!!.add(Tag(dc.document.id, dc.document.get("name").toString()))
                    }
                }
            }
        })

        binding.backButton.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        binding.addNewImageBtn.setOnClickListener{
            choosePhoto()
        }

        binding.submit.setOnClickListener{
            // Bind all data to here
            val name = binding.editRecipeName
            val description = binding.editRecipeDescription
            val notes = binding.editRecipeNotes
            val ingredients = binding.editAddNewIngredients
            val extraInformation = binding.editRecipeExtraInfo
            val link = binding.editRecipeVideoLink
            val status = "Active"
            val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
            val sharedUid = sharedPreferences.getString("StringUid", null)

            val photo = uploadPhoto()
        }
    }

    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val bitmap = it?.data?.extras?.get("data") as Bitmap
        recipe_photo.setImageBitmap((bitmap))
    }

    private fun choosePhoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
//        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
//        startActivityForResult(gallery, pickImage)

//        val intent = Intent(Intent.ACTION_PICK)
//        intent.type = "image/*"
//        startActivityForResult(intent, IMAGE_PICK_CODE)
        startActivityForResult(intent, 100)
    }

    private fun uploadPhoto() {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(ImageUri).addOnSuccessListener {
            binding.imageViewRecipePhoto.setImageURI(null)
        }
            .addOnFailureListener{

            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ImageUri = data?.data!!
            binding.imageViewRecipePhoto.visibility = View.VISIBLE
            binding.imageViewRecipePhoto.setImageURI(ImageUri)
        }
    }
}