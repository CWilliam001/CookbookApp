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
//    private var tagList = ArrayList<Int>()
//    private var tagNameList = ArrayList<String>()
//    private var tagContent = ArrayList<String>()
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
        val query1 : Query = db.collection("User")
        val query2: Query = db.collection("Tag")

        // Get Recipe Data
        query.whereEqualTo("id", recipeId).get().addOnSuccessListener {
            for (document in it) {
                binding.textViewViewRecipeName.text = document.get("name").toString()
                photo = document.get("photo").toString()
                binding.textViewDescriptionContent.text = document.get("description").toString()
                binding.textViewNotesContent.text = document.get("notes").toString()
                binding.textViewIngredientsContent.text = document.get("ingredients").toString()
                binding.textViewExtraInformationContent.text = document.get("extraInformation").toString()
                if(document.get("link").toString() == "") {
                    binding.textViewViewLink.visibility = View.GONE
                    binding.textViewLinkContent.visibility = View.GONE
                } else {
                    binding.textViewLinkContent.text = document.get("link").toString()
                }
                uid = document.get("userID").toString()
                binding.textViewUserName.text = "By User ID: $uid"
//                tagList = document.get("tagList") as ArrayList<Int>
                if (uid == sharedUid) {
                    // Display the button if the recipe owner has visit this page
                    binding.buttonToEditRecipe.visibility = View.VISIBLE
                }

//                // Get User Data
//                query1.whereEqualTo("id", uid).get().addOnSuccessListener {
//                    for (document in it) {
//                        firstName = document.get("firstName").toString()
//                        lastName = document.get("lastName").toString()
//                        Toast.makeText(this, "$firstName data loaded", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                    .addOnFailureListener{
//                            e ->
//                        Toast.makeText(this, "User Firestore Error : ${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//                binding.textViewUserName.text = "By $firstName"

//                // Get a list of Tag
//                query2.addSnapshotListener(object : EventListener<QuerySnapshot> {
//                    override fun onEvent(
//                        value: QuerySnapshot?,
//                        error: FirebaseFirestoreException?
//                    ) {
//                        if (error != null) {
//                            Log.e("Firestore error", error.message.toString())
//                            return
//                        }
//
//                        for (dc : DocumentChange in value?.documentChanges!!){
//                            if (dc.type == DocumentChange.Type.ADDED){
//                                tagNameList.add(dc.document.get("name").toString())
//                            }
//                        }
//                    }
//                })

//                for (i in 1..tagNameList.size) {
//                    if (tagList[i] == i) {
//                        tagContent.add(tagNameList[i])
//                    }
//                }
//
//                for(i in 0..tagContent.size) {
//                    tagString += "$tagContent"
//                    if (i <= tagContent.size) {
//                        tagString += ", "
//                    }
//                }
            }
        }
            .addOnFailureListener{
                e ->
                Log.e("Firestore Error: ", e.message.toString())
            }

        // Get image from Storage
        val storageRef = FirebaseStorage.getInstance().reference.child("images/media/$photo")
        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.imageViewViewRecipePhoto.visibility = View.VISIBLE
            binding.imageViewViewRecipePhoto.setImageBitmap(bitmap)
        }
            .addOnFailureListener{
                e ->
                Toast.makeText(this, "Storage error : ${e.message}", Toast.LENGTH_SHORT).show()
            }

        binding.buttonViewRecipeBackToDashboard.setOnClickListener{
            startActivity(Intent(this, Dashboard::class.java))
            finish()
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