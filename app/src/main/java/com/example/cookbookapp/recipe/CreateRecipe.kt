package com.example.cookbookapp.recipe

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.CreateRecipeBinding
import com.example.cookbookapp.model.Tag
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CreateRecipe : AppCompatActivity() {

    private lateinit var binding: CreateRecipeBinding
    private lateinit var recipe_photo : ImageView
    private lateinit var ImageUri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CreateRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var tagListView = binding.listViewTags as ListView

        var tagDataModel = ArrayList<Tag>()
        // tagDataModel!!.add(Tag())

        binding.addNewImageBtn.setOnClickListener{
            choosePhoto()
        }

        binding.submit.setOnClickListener{
            // Bind all data to here

            uploadPhoto()
        }
    }

    val getAction = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val bitmap = it?.data?.extras?.get("data") as Bitmap
        recipe_photo.setImageBitmap((bitmap))
    }

    private fun choosePhoto() {
        val intent = Intent()
        intent.type = "images/*"
        intent.action = Intent.ACTION_GET_CONTENT

        if (intent.resolveActivity(packageManager) != null) {
            getAction.launch((intent))
            recipe_photo = binding.imageViewRecipePhoto
        }
    }

    private fun uploadPhoto() {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(ImageUri).addOnSuccessListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            recipe_photo.setImageURI(ImageUri)
        }
    }
}