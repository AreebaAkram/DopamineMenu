package com.example.dopaminemenu.vibemenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminemenu.databinding.TasksActivityBinding
import com.example.dopaminemenu.vibemenu.model.Activity
import com.example.dopaminemenu.vibemenu.model.ActivityState
import com.google.firebase.database.DatabaseReference

class ActivitiesAdapter(val activities: MutableList<Activity>, private val database : DatabaseReference) :
    RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>() {


    private lateinit var context: Context

    inner class ViewHolder(val binding: TasksActivityBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(item: Activity) {
                binding.activity = item
                binding.executePendingBindings()
                binding.deletebtn.setOnClickListener{
                    database.child("activities").child(item.name.toString()).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
                            activities.remove(item)
                            notifyDataSetChanged()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
                        }
            }
                binding.checkbox.setOnCheckedChangeListener(null)
                binding.checkbox.isChecked = item.state == ActivityState.completed

                binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        item.state = ActivityState.completed
                        item.completedAt = System.currentTimeMillis()
                        database.child("activities").child(item.name.toString())
                            .setValue(item)   // save full object with state + completedAt
                            .addOnSuccessListener {
                                Toast.makeText(context, "Completed ${item.name}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        item.state = ActivityState.pending
                        item.completedAt = null  // reset timestamp when going back to pending

                        database.child("activities").child(item.name.toString())
                            .setValue(item)   // save full object with state reset
                    }}
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
        holder.bind(item)

    }
}