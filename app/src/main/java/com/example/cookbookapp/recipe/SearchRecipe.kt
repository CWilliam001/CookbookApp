package com.example.cookbookapp.recipe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.R
import com.example.cookbookapp.databinding.SearchRecipeBinding
import com.example.cookbookapp.model.Recipe
import com.google.firebase.firestore.*
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

class SearchRecipe : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore
    private lateinit var searchRecyclerView: RecyclerView
    private lateinit var searchArrayList: ArrayList<Recipe>
    private lateinit var tempArrayList: ArrayList<Recipe>
    private lateinit var mySearchAdapter: AdapterSearch
    private lateinit var binding: SearchRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SearchRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back Button onClick
        val backBtn = findViewById<Button>(R.id.searchBackToDashboard)

        backBtn.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }
        // End of Back Button onClick

        searchRecyclerView = findViewById(R.id.searchRecyclerView)
        searchRecyclerView.layoutManager = LinearLayoutManager(this)
        searchRecyclerView.setHasFixedSize(true)

        searchArrayList = arrayListOf<Recipe>()
        tempArrayList = arrayListOf<Recipe>()
        mySearchAdapter = AdapterSearch(tempArrayList)
        searchRecyclerView.adapter = mySearchAdapter
        getRecipeListData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item, menu)
        val item = menu?.findItem(R.id.search_action)
        val searchView = item?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tempArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())

                if (searchText.isNotEmpty()){
                    searchArrayList.forEach{
                        if (it.name!!.lowercase()!!.contains(searchText)) {
                            tempArrayList.add(it)
                        }
                    }

                    searchRecyclerView.adapter!!.notifyDataSetChanged()

                } else {
                    tempArrayList.clear()
                    tempArrayList.addAll(searchArrayList)
                    searchRecyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun getRecipeListData() {
        db = FirebaseFirestore.getInstance()

        db.collection("Recipe").orderBy("name").addSnapshotListener(object : EventListener<QuerySnapshot> {
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
                        searchArrayList.add(dc.document.toObject(Recipe::class.java))
                    }
                }

                tempArrayList.addAll(searchArrayList)

                searchRecyclerView.adapter!!.notifyDataSetChanged()
            }
        })
    }
}