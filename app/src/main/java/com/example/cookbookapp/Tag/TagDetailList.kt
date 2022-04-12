package com.example.cookbookapp.Tag

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.TagDetailListingBinding
import com.example.cookbookapp.model.Recipe
import com.example.cookbookapp.model.Tag
import com.example.cookbookapp.recipe.ViewRecipe
import com.google.firebase.firestore.*

class TagDetailList : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var tagListRecyclerView: RecyclerView
    private lateinit var tagDetailArrayList: ArrayList<Recipe>
    private lateinit var myTagDetailAdapter: AdapterTagDetailList
    private lateinit var binding: TagDetailListingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = TagDetailListingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("sharedUid", Context.MODE_PRIVATE)

        // Back Button onClick
        val backBtn = findViewById<Button>(R.id.tagListBackToDashboard)

        backBtn.setOnClickListener {
            startActivity(Intent(this, TagList::class.java))
            finish()
        }
        // End of Back Button onClick

        tagListRecyclerView = findViewById(R.id.tagListRecyclerView)
        tagListRecyclerView.layoutManager = LinearLayoutManager(this)
        tagListRecyclerView.setHasFixedSize(true)

        tagDetailArrayList = arrayListOf<Recipe>()
        myTagDetailAdapter = AdapterTagDetailList(tagDetailArrayList, this@TagDetailList::onItemClickHandler3)
        tagListRecyclerView.adapter = myTagDetailAdapter

        getRecipeListData()
    }

    private fun getRecipeListData() {
        db = FirebaseFirestore.getInstance()

        val tagID = Integer.parseInt(intent.getStringExtra("id"))

        db.collection("Recipe").whereArrayContains("tagList", tagID).addSnapshotListener(object :
            EventListener<QuerySnapshot> {
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
                        tagDetailArrayList.add(dc.document.toObject(Recipe::class.java))
                    }
                }

                tagListRecyclerView.adapter!!.notifyDataSetChanged()
            }
        })
    }

    private fun onItemClickHandler3(position:Int){
        Log.d("******","${position}");
        //here you can start a new intent to open a new activity on click of item
        val intent = Intent(this, ViewRecipe::class.java)
        intent.putExtra("id", tagDetailArrayList.get(position).id)
        startActivity(intent)
    }
}