package com.example.dopaminemenu.vibemenu.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminemenu.databinding.HomeActivityBinding
import com.example.dopaminemenu.vibemenu.model.Category

class MainAdapter(
    val categories: MutableList<Category>, private val onItemClick: (Category) -> Unit,
    private val onbtnclick: (Category) -> Unit
) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private lateinit var context: Context


    inner class ViewHolder(val binding: HomeActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Category) {
            binding.categoryitem = item
            binding.root.setOnClickListener {
                onItemClick(item)
            }
            binding.addbtn.setOnClickListener {
                onbtnclick(item)
            }
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        context = parent.context
        val binding =
            HomeActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        val item = categories[position]
        holder.bind(item)

        val colors = listOf(
            android.graphics.Color.parseColor("#E09BA0"),
            android.graphics.Color.parseColor("#D4B06B"),
            android.graphics.Color.parseColor("#A67D8A"),
            android.graphics.Color.parseColor("#A3B18A"),
            android.graphics.Color.parseColor("#EEC9A1"),
        )
        val addbg = holder.binding.addbtn.background.mutate() as GradientDrawable
        val color = (colors[position % colors.size])
        addbg.setColor(color)
    }

    override fun getItemCount(): Int = categories.size

}

