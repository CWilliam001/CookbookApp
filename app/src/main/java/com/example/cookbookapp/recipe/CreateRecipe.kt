package com.example.cookbookapp.recipe

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.se.omapi.Session
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.R
import com.example.cookbookapp.adapter.TagCheckboxRecyclerViewAdapter
import com.example.cookbookapp.databinding.CreateRecipeBinding
import com.example.cookbookapp.model.Recipe
import com.example.cookbookapp.model.RecipeTag
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
    private var selectedList = ArrayList<Int>()
    val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
            val now = Date()
            val id = formatter.format(now)
            val name = binding.createRecipeName.text.toString().trim()
            val description = binding.createRecipeDescription.text.toString().trim()
            val notes = binding.createRecipeNotes.text.toString().trim()
            val link = binding.createRecipeVideoLink.text.toString().trim()
            val extraInformation = binding.createRecipeExtraInfo.text.toString().trim()
            val ingredients = binding.createIngredients.text.toString().trim()
            val status = "Active"
            val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)
            val sharedUid = sharedPreferences.getString("StringUid", null).toString()
            val photo = uploadPhoto()
            selectedList = tagAdapter.getSelectedCheckboxList()

            if (TextUtils.isEmpty(name)) {
                binding.createRecipeName.error = "Please enter the recipe name"
            } else if (TextUtils.isEmpty(description)) {
                binding.createRecipeDescription.error = "Please enter the recipe description"
            } else if (TextUtils.isEmpty(notes)) {
                binding.createRecipeNotes.error = "Please enter the recipe notes"
            } else if (selectedList.isEmpty()) {
                binding.textViewTagListErrorMessage.visibility = View.VISIBLE
                binding.textViewTagListErrorMessage.error = "Please select at least one tag"
            } else if (TextUtils.isEmpty(photo)) {
                binding.textViewPhotoErrorMessage.visibility = View.VISIBLE
                binding.textViewPhotoErrorMessage.error = "Please select a photo"
            } else if (TextUtils.isEmpty(extraInformation)) {
                binding.createRecipeExtraInfo.error = "Please enter the recipe steps"
            } else if (TextUtils.isEmpty(ingredients)) {
                binding.createIngredients.error = "Please enter the recipe ingredients"
            } else {
                val recipe = Recipe(id, description, extraInformation, ingredients, link, name, notes, photo, sharedUid, status, selectedList)
                db.collection("Recipe").document(id).set(recipe)

                var intent = Intent(this, ViewRecipe::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
                finish()
            }
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
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun uploadPhoto() : String {
        if (ImageUri != null) {
            var storageRef = FirebaseStorage.getInstance().reference.child("images/$ImageUri")
            storageRef.putFile(ImageUri).addOnSuccessListener {
                Toast.makeText(this, "Image upload successfully", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener{
                    e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
        }
        return ImageUri.toString().substringAfterLast("/")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            ImageUri = data.data!!
            binding.imageViewRecipePhoto.visibility = View.VISIBLE
            binding.imageViewRecipePhoto.setImageURI(data.data) // handle chosen image
        }
    }
}