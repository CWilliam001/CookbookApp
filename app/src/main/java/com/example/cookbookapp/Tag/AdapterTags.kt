package com.example.cookbookapp.Tag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.R
import com.example.cookbookapp.model.Tag

class AdapterTags(private val tagList: ArrayList<Tag>, val itemClickHandler:(Int) -> Unit) : RecyclerView.Adapter<AdapterTags.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tags_grid_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTag = tagList[position]
        holder.tagName.text = currentTag.name
        holder.tagID.text = currentTag.id

        holder.itemView.setOnClickListener {
            itemClickHandler.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagName: TextView = itemView.findViewById(R.id.tagName)
        val tagID: TextView = itemView.findViewById(R.id.tagID)
    }

}