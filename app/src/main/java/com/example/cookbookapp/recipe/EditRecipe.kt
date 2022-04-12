package com.example.cookbookapp.recipe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.R
import com.example.cookbookapp.adapter.TagCheckboxRecyclerViewAdapter
import com.example.cookbookapp.databinding.EditRecipeBinding
import com.example.cookbookapp.model.Recipe
import com.example.cookbookapp.model.Tag
import com.example.cookbookapp.model.TagData
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class EditRecipe : AppCompatActivity() {

    private lateinit var binding: EditRecipeBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var recipe_photo : ImageView
    private lateinit var tagAdapter: TagCheckboxRecyclerViewAdapter
    private lateinit var ImageUri : Uri
    private lateinit var tagArrayList: ArrayList<Tag>
    private lateinit var recyclerView_tag_checkbox: RecyclerView

    private var selectedList = ArrayList<Int>()
    val REQUEST_CODE = 100

    private var id = ""
    private var description = ""
    private var extraInformation = ""
    private var ingredients = ""
    private var link = ""
    private var name = ""
    private var notes = ""
    private var photo = ""
    private var userID = ""
    private var status = ""
    private var tagList = ArrayList<Int>()

    private var photoOnChange = false

    private var edited_description = ""
    private var edited_extraInformation = ""
    private var edited_ingredients = ""
    private var edited_link = ""
    private var edited_name = ""
    private var edited_notes = ""
    private var edited_photo = ""
    private var edited_tagList = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        id = intent.getStringExtra("id").toString()

        // Retrieve data from firebase
        db.collection("Recipe").whereEqualTo("id", id).get().addOnSuccessListener {
            for (document in it) {
                binding.editRecipeName.setText(document.get("name").toString())
                binding.editRecipeDescription.setText(document.get("description").toString())
                binding.editRecipeNotes.setText((document.get("notes").toString()))
                tagList = document.get("tagList") as ArrayList<Int>
                binding.editRecipeVideoLink.setText(document.get("link").toString())
                binding.editRecipeExtraInfo.setText(document.get("extraInformation").toString())
                binding.editAddNewIngredients.setText(document.get("ingredients").toString())
                userID = document.get("userID").toString()
                status = document.get("status").toString()
                photo = document.get("photo").toString()

                // Load image
                val storageRef = FirebaseStorage.getInstance().reference.child("images/$photo")
                val localFile = File.createTempFile("temp", "jpg")
                storageRef.getFile(localFile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    binding.imageViewRecipePhoto.visibility = View.VISIBLE
                    binding.imageViewRecipePhoto.setImageBitmap(bitmap)
                    Log.e("Error 2", it.toString())

                }
                    .addOnFailureListener{
                        Log.e("Error", it.toString())
                        Toast.makeText(this, "Error : ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Tag List
        recyclerView_tag_checkbox = binding.recyclerViewEditTagCheckbox
        recyclerView_tag_checkbox.layoutManager = GridLayoutManager(this, 1, RecyclerView.VERTICAL, false)
        recyclerView_tag_checkbox.setHasFixedSize(true)

        tagArrayList = arrayListOf<Tag>()
        tagAdapter = TagCheckboxRecyclerViewAdapter(tagArrayList)
        recyclerView_tag_checkbox.adapter = tagAdapter
        getTagsData()

        binding.backButton.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
            Toast.makeText(this, "Edit recipe cancelled", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.editImageBtn.setOnClickListener{
            choosePhoto()
        }

        binding.buttonDelete.setOnClickListener{
            val intent = Intent(this, DeleteRecipe::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        binding.buttonSave.setOnClickListener{
            edited_name = binding.editRecipeName.text.toString().trim()
            edited_description = binding.editRecipeDescription.text.toString().trim()
            edited_notes = binding.editRecipeNotes.text.toString().trim()
            edited_tagList = tagAdapter.getSelectedCheckboxList()
            edited_link = binding.editRecipeVideoLink.text.toString().trim()
            edited_extraInformation = binding.editRecipeExtraInfo.text.toString().trim()
            edited_ingredients = binding.editAddNewIngredients.text.toString().trim()

            if (TextUtils.isEmpty(edited_name)) {
                binding.editRecipeName.error = "Please enter the recipe name"
            } else if (TextUtils.isEmpty(edited_description)) {
                binding.editRecipeDescription.error = "Please enter the recipe description"
            } else if (TextUtils.isEmpty(edited_notes)) {
                binding.editRecipeNotes.error = "Please enter the recipe notes"
            } else if (TextUtils.isEmpty(edited_extraInformation)) {
                binding.editRecipeExtraInfo.error = "Please enter the recipe steps"
            } else if (TextUtils.isEmpty(edited_ingredients)) {
                binding.editAddNewIngredients.error = "Please enter the recipe ingredients"
            } else{
                var recipe = Recipe()
                if (photoOnChange) {
                    val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
                    val now = Date()
                    val fileName = formatter.format(now) + ".jpg"
                    val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")
                    storageReference.putFile(ImageUri).addOnSuccessListener {

                        // If the selected list is empty then it will use back the tagList from Firebase
                        if (selectedList.isEmpty()) {
                            selectedList = tagList
                        }
                        recipe = Recipe(id, edited_description, edited_extraInformation, edited_ingredients, edited_link, edited_name, edited_notes, fileName, userID, status, selectedList)
                        db.collection("Recipe").document(id).set(recipe)

                        val intent = Intent(this, ViewRecipe::class.java)
                        Toast.makeText(this, "Recipe has been modified", Toast.LENGTH_SHORT).show()
                        intent.putExtra("id", id)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    if (selectedList.isEmpty()) {
                        selectedList = tagList
                    }
                    recipe = Recipe(id, edited_description, edited_extraInformation, edited_ingredients, edited_link, edited_name, edited_notes, photo, userID, status, selectedList)
                    db.collection("Recipe").document(id).set(recipe)

                    val intent = Intent(this, ViewRecipe::class.java)
                    Toast.makeText(this, "Recipe has been modified", Toast.LENGTH_SHORT).show()
                    intent.putExtra("id", id)
                    startActivity(intent)
                    finish()
                }




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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            ImageUri = data.data!!
            binding.imageViewRecipePhoto.visibility = View.VISIBLE
            photoOnChange = true
            binding.imageViewRecipePhoto.setImageURI(data.data) // handle chosen image
        }
    }
}