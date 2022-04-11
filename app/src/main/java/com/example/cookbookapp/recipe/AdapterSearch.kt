package com.example.cookbookapp.recipe

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.R
import com.example.cookbookapp.recipe.AdapterSearch
import com.example.cookbookapp.model.Recipe

class AdapterSearch(private val searchList: ArrayList<Recipe>) :
    RecyclerView.Adapter<AdapterSearch.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_recipe_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRecipe = searchList[position]
        holder.searchRecipeName.text = currentRecipe.name
        holder.searchRecipeDescription.text = currentRecipe.description
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchRecipeName: TextView = itemView.findViewById(R.id.searchListRecipeName)
        val searchRecipeDescription: TextView =
            itemView.findViewById(R.id.searchListRecipeDescription)
    }

}