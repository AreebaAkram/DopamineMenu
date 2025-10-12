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

class ActivitiesAdapter(
    val activities: MutableList<Activity>,
    private val database: DatabaseReference
) :
    RecyclerView.Adapter<ActivitiesAdapter.ViewHolder>() {


    private lateinit var context: Context

    inner class ViewHolder(val binding: TasksActivityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Activity) {
            binding.activity = item
            binding.executePendingBindings()

            if (item.state == ActivityState.completed) {
                binding.taskname.paintFlags = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                binding.taskname.paintFlags = 0
            }
            binding.deletebtn.setOnClickListener {
                val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                if (userId.isNullOrEmpty()) {
                    Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                database.child("users").child(userId).child("activities")
                    .orderByChild("name").equalTo(item.name)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            for (child in snapshot.children) {
                                child.ref.removeValue()
                            }
                            activities.remove(item)
                            notifyDataSetChanged()
                            Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(context, "Activity not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Delete failed: ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
            }

//            binding.deletebtn.setOnClickListener{
//                database.child("activities").child(item.name.toString()).removeValue()
//                    .addOnSuccessListener {
//                        Toast.makeText(context, "Deleted ${item.name}", Toast.LENGTH_SHORT).show()
//                        activities.remove(item)
//                        notifyDataSetChanged()
//                    }
//                    .addOnFailureListener {
//                        Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
//                    }
//            }
            binding.checkbox.setOnCheckedChangeListener(null)
            binding.checkbox.isChecked = item.state == ActivityState.completed

            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
                if (userId.isNullOrEmpty()) return@setOnCheckedChangeListener

                val newState = if (isChecked) ActivityState.completed else ActivityState.pending
                val completedTime: Long? = if (isChecked) System.currentTimeMillis() else null

                database.child("users").child(userId).child("activities")
                    .orderByChild("name").equalTo(item.name)
                    .get()
                    .addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            for (child in snapshot.children) {
                                child.ref.child("state").setValue(newState.name)
                                child.ref.child("completedAt").setValue(completedTime)
                            }

                            item.state = newState
                            item.completedAt = completedTime
                            notifyDataSetChanged()

                            val msg =
                                if (isChecked) "Completed ${item.name}" else "Marked ${item.name} pending"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Activity not found in DB", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Update failed: ${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
            }


//            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
//                if (isChecked) {
//                    item.state = ActivityState.completed
//                    item.completedAt = System.currentTimeMillis()
//
//                    database.child("Activities").child(item.name.toString())
//                        .setValue(item)
//                        .addOnSuccessListener {
//                            Toast.makeText(context, "Completed ${item.name}", Toast.LENGTH_SHORT).show()
//                        }
//                } else {
//                    item.state = ActivityState.pending
//                    item.completedAt = null
//
//                    database.child("Activities").child(item.name.toString())
//                        .setValue(item)
//                }}
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