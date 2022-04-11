package com.example.cookbookapp.adapter

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cookbookapp.R
import com.example.cookbookapp.model.Tag
import com.example.cookbookapp.model.TagData
import kotlinx.android.synthetic.main.create_recipe.view.*
import kotlinx.android.synthetic.main.tag_checkbox_row.view.*

class TagCheckboxRecyclerViewAdapter(private val list: ArrayList<Tag>): RecyclerView.Adapter<TagCheckboxRecyclerViewAdapter.ViewHolder>() {
    var tagCheckBoxStateArray = SparseBooleanArray()
    var selectedCheckbox = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val  inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.tag_checkbox_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.checkbox.isChecked = tagCheckBoxStateArray.get(position, false)
        var currentTag = list[position]
        holder.checkbox.text = currentTag.name
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var checkbox = itemView.tags_checkbox

        init {
            checkbox.setOnClickListener{
                if (!tagCheckBoxStateArray.get(absoluteAdapterPosition, false)) {
                    checkbox.isChecked = true
                    tagCheckBoxStateArray.put(absoluteAdapterPosition, true)
                    selectedCheckbox.add(absoluteAdapterPosition)
                } else {
                    checkbox.isChecked = false
                    tagCheckBoxStateArray.put(absoluteAdapterPosition, false)
                    selectedCheckbox.remove(absoluteAdapterPosition)
                }
            }
        }
    }

    fun getSelectedCheckboxList() : ArrayList<Int> {
        return selectedCheckbox
    }

}