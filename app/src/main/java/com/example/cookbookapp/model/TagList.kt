package com.example.cookbookapp.model

import android.os.Bundle
import android.util.Log
import android.widget.Adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.R
import com.google.firebase.firestore.*

class TagList : AppCompatActivity() {
    private lateinit var db : FirebaseFirestore
    private lateinit var tagRecyclerView: RecyclerView
    private lateinit var tagArrayList: ArrayList<Tag>
    private lateinit var myTagAdapter: AdapterTags

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tag_search)

        tagRecyclerView = findViewById(R.id.tagsRecyclerView)
        tagRecyclerView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        tagRecyclerView.setHasFixedSize(true)

        tagArrayList = arrayListOf<Tag>()
        myTagAdapter = AdapterTags(tagArrayList)
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

}