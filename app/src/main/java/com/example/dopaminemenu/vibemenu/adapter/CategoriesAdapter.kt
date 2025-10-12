package com.example.dopaminemenu.vibemenu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dopaminemenu.databinding.DelcategoryBinding
import com.example.dopaminemenu.vibemenu.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DeleteCategoryAdapter(private val categories: MutableList<Category>) :
    RecyclerView.Adapter<DeleteCategoryAdapter.ViewHolder>() {

    private lateinit var context: Context
    private val database = FirebaseDatabase.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    inner class ViewHolder(val binding: DelcategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.category = category
            binding.executePendingBindings()

            binding.deletebtn.setOnClickListener {
                if (userId == null) return@setOnClickListener

                database.child("users").child(userId).child("customCategories")
                    .orderByChild("name").equalTo(category.name)
                    .addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
                        override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                            for (child in snapshot.children) {
                                child.ref.removeValue()
                            }
                            Toast.makeText(context, "Deleted ${category.name}", Toast.LENGTH_SHORT).show()
                            categories.remove(category)
                            notifyDataSetChanged()
                        }

                        override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                            Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = DelcategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }
}
