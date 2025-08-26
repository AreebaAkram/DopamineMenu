package com.example.dopaminemenu.vibemenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminemenu.databinding.HomeActivityBinding
import com.example.dopaminemenu.vibemenu.model.Category

class MainAdapter(val categories: MutableList<Category>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private lateinit var context: Context


    inner class ViewHolder(val binding: HomeActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        context = parent.context
        val binding =
            HomeActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        val item = categories[position]
        holder.binding.categoryitem = item
        holder.binding.executePendingBindings()


//        val colors = listOf(
//            android.graphics.Color.parseColor("#A2AF9B"),
//            android.graphics.Color.parseColor("#D5C7A3"),
//            android.graphics.Color.parseColor("#B4B4B8"),
//            android.graphics.Color.parseColor("#607274"),
//            android.graphics.Color.parseColor("#BDB395"),
//        )
//        holder.binding.root.setBackgroundColor(colors[position % colors.size])
    }

    override fun getItemCount(): Int = categories.size

}

