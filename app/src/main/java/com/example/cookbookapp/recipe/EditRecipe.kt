package com.example.cookbookapp.recipe

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.R
import com.example.cookbookapp.adapter.TagCheckboxRecyclerViewAdapter
import com.example.cookbookapp.databinding.EditRecipeBinding
import com.example.cookbookapp.model.Tag
import com.example.cookbookapp.model.TagData
import com.google.firebase.firestore.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        id = intent.getStringExtra("id").toString()

        db.collection("Recipe").whereEqualTo("id", id).get().addOnSuccessListener {
            for (document in it) {
                binding.editRecipeName.setText(document.get("name").toString())
                binding.editRecipeDescription.setText(document.get("description").toString())
                binding.editRecipeNotes.setText((document.get("notes").toString()))
                tagList = document.get("tagList").toString() as ArrayList<Int>

            }
        }

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
            binding.imageViewRecipePhoto.setImageURI(data.data) // handle chosen image
        }
    }
}