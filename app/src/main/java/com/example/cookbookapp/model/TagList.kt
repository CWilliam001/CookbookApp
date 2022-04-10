package com.example.cookbookapp.model

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.R
import com.google.firebase.database.*

class TagList : AppCompatActivity() {
    private lateinit var db : DatabaseReference
    private lateinit var tagRecyclerView: RecyclerView
    private lateinit var tagArrayList: ArrayList<Tag>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tag_search)

        tagRecyclerView = findViewById(R.id.tagsRecyclerView)
        tagRecyclerView.layoutManager = GridLayoutManager(this, 2)
        tagRecyclerView.setHasFixedSize(true)

        tagArrayList = arrayListOf<Tag>()
        getTagsData()

    }

    private fun getTagsData() {
        db = FirebaseDatabase.getInstance().getReference("Tag")

        db.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (tagSnapshot in snapshot.children){
                        val tag = tagSnapshot.getValue(Tag::class.java)
                        tagArrayList.add(tag!!)
                    }

                    tagRecyclerView.adapter = AdapterTags(tagArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

}