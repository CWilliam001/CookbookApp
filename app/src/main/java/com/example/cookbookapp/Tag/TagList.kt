package com.example.cookbookapp.Tag

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.Dashboard
import com.example.cookbookapp.R
import com.example.cookbookapp.Tag.AdapterTags
import com.example.cookbookapp.account.ViewProfile
import com.example.cookbookapp.model.Tag
import com.google.firebase.firestore.*

class TagList : AppCompatActivity() {
    private lateinit var db : FirebaseFirestore
    private lateinit var tagRecyclerView: RecyclerView
    private lateinit var tagArrayList: ArrayList<Tag>
    private lateinit var myTagAdapter: AdapterTags

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tag_search)

        val backBtn = findViewById<Button>(R.id.tagBackToDashboard)

        backBtn.setOnClickListener {
            startActivity(Intent(this, Dashboard::class.java))
            finish()
        }

        tagRecyclerView = findViewById(R.id.tagsRecyclerView)
        tagRecyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        tagRecyclerView.setHasFixedSize(true)

        tagArrayList = arrayListOf<Tag>()
        myTagAdapter = AdapterTags(tagArrayList, this@TagList::onItemClickHandler)
        tagRecyclerView.adapter = myTagAdapter
        getTagsData()

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

                myTagAdapter.notifyDataSetChanged()
            }
        })
    }

    private fun onItemClickHandler(position:Int){
        Log.d("******","${position}");
        //here you can start a new intent to open a new activity on click of item
        val intent = Intent(this, TagDetailList::class.java)
        intent.putExtra("id", tagArrayList.get(position).id)
        startActivity(intent)
    }

}