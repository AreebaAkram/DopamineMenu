package com.example.dopaminemenu.vibemenu.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminemenu.databinding.HomeActivityBinding
import com.example.dopaminemenu.vibemenu.model.Category


class CategoryAdapter : ListAdapter <Category, CategoryAdapter.CategoryViewHolder> (diffChecker){

    inner class CategoryViewHolder(var binding : HomeActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding  : HomeActivityBinding =
            HomeActivityBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding = binding)
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val dataitem = getItem(position)
        holder.binding.categoryitem = dataitem
//
//        val colors = listOf(
//            android.graphics.Color.parseColor("#A2AF9B"),
//            android.graphics.Color.parseColor("#D5C7A3"),
//            android.graphics.Color.parseColor("#B4B4B8"),
//            android.graphics.Color.parseColor("#607274"),
//            android.graphics.Color.parseColor("#BDB395"),
//        )
//        holder.binding.root.setBackgroundColor(colors[position % colors.size])
    }
}
val diffChecker =  object: DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return false
    }
}