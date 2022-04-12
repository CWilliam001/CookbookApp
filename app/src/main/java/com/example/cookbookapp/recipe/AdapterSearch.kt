package com.example.cookbookapp.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.R
import com.example.cookbookapp.model.Recipe

class AdapterSearch(private val searchList: ArrayList<Recipe>, val itemClickHandler2:(Int) -> Unit) : RecyclerView.Adapter<AdapterSearch.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_recipe_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRecipe = searchList[position]
        holder.searchRecipeName.text = currentRecipe.name
        holder.searchRecipeDescription.text = currentRecipe.description
        holder.searchRecipeID.text = currentRecipe.id

        holder.itemView.setOnClickListener {
            itemClickHandler2.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchRecipeName: TextView = itemView.findViewById(R.id.tagListRecipeName)
        val searchRecipeDescription: TextView = itemView.findViewById(R.id.tagListRecipeDescription)
        val searchRecipeID: TextView = itemView.findViewById(R.id.searchRecipeID)
    }

}