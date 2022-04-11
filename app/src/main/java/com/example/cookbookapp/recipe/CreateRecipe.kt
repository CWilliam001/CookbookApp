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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.R
import com.example.cookbookapp.adapter.TagCheckboxRecyclerViewAdapter
import com.example.cookbookapp.databinding.CreateRecipeBinding
import com.example.cookbookapp.model.AdapterTags
import com.example.cookbookapp.model.Recipe
import com.example.cookbookapp.model.Tag
import com.example.cookbookapp.model.TagData
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
    private lateinit var tagAdapter: TagCheckboxRecyclerViewAdapter
    private lateinit var ImageUri : Uri
    private lateinit var tagArrayList: ArrayList<Tag>
    private lateinit var recyclerView_tag_checkbox: RecyclerView

    private var tagList = ArrayList<TagData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        setupData()
        recyclerView_tag_checkbox = binding.recyclerViewTagCheckbox
        recyclerView_tag_checkbox.layoutManager = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)
        recyclerView_tag_checkbox.setHasFixedSize(true)

        tagArrayList = arrayListOf<Tag>()
        tagAdapter = TagCheckboxRecyclerViewAdapter(tagArrayList)
        recyclerView_tag_checkbox.adapter = tagAdapter
        getTagsData()

        binding.backButton.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        binding.addNewImageBtn.setOnClickListener{
            choosePhoto()
        }

        binding.submit.setOnClickListener{
            // Bind all data to here
            val name = binding.editRecipeName.toString().trim()
            val description = binding.editRecipeDescription.toString().trim()
            val notes = binding.editRecipeNotes.toString().trim()
            val ingredients = binding.editAddNewIngredients.toString().trim()
            val extraInformation = binding.editRecipeExtraInfo.toString().trim()
            val link = binding.editRecipeVideoLink.toString().trim()
            val status = "Active"
            val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
            val sharedUid = sharedPreferences.getString("StringUid", null).toString()
            val photo = uploadPhoto()

            val recipe = Recipe("", description, extraInformation, ingredients, link, name, notes, photo, sharedUid)
            db.collection("Recipe").document().set(recipe)

            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }
    }

    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val bitmap = it?.data?.extras?.get("data") as Bitmap
        recipe_photo.setImageBitmap((bitmap))
    }

    private fun getTagsData() {
        db = FirebaseFirestore.getInstance()

        db.collection("Tag").addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if (error != null) {
                    Log.e("Firestore error", error.message.toString())
                    return
                }

                for (dc : DocumentChange in value?.documentChanges!!){
                    if (dc.type == DocumentChange.Type.ADDED){
                        tagArrayList.add(dc.document.toObject(Tag::class.java))
                    }
                }

                tagAdapter.notifyDataSetChanged()
            }

        })
    }

    private fun choosePhoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    private fun uploadPhoto() : String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(ImageUri).addOnSuccessListener {
            binding.imageViewRecipePhoto.setImageURI(null)
        }
        .addOnFailureListener{

        }
        return ImageUri.toString()
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