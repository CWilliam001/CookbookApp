package com.example.cookbookapp.Tag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.R
import com.example.cookbookapp.model.Recipe

class AdapterTagDetailList(private val tagDetailList: ArrayList<Recipe>, val itemClickHandler3:(Int) -> Unit) : RecyclerView.Adapter<AdapterTagDetailList.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.tag_detail_listing_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTagDetailList = tagDetailList[position]
        holder.tagDetailRecipeName.text = currentTagDetailList.name
        holder.tagDetailListDescription.text = currentTagDetailList.description
        holder.tagDetailRecipeID.text = currentTagDetailList.id

        holder.itemView.setOnClickListener {
            itemClickHandler3.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return tagDetailList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tagDetailRecipeName: TextView = itemView.findViewById(R.id.tagListRecipeName)
        val tagDetailListDescription: TextView = itemView.findViewById(R.id.tagListRecipeDescription)
        val tagDetailRecipeID: TextView = itemView.findViewById(R.id.tagListRecipeID)
    }


}
