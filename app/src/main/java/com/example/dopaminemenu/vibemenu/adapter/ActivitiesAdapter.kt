package com.example.dopaminemenu.vibemenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminemenu.databinding.TasksActivityBinding
import com.example.dopaminemenu.vibemenu.model.Activity

class ActivitiesAdapter(val activities: MutableList<Activity>) :
    RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>() {


    private lateinit var context: Context

    inner class ViewHolder(val binding: TasksActivityBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(item: Activity) {
                binding.activity = item
                binding.executePendingBindings()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
      context = parent.context
        val binding =
            TasksActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return activities.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = activities[position]
        holder.binding.activity = item
        holder.binding.executePendingBindings()

    }
}