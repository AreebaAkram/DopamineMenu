package com.example.dopaminemenu.vibemenu.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dopaminemenu.R
import com.example.dopaminemenu.databinding.DelcatrecyclerviewBinding
import com.example.dopaminemenu.vibemenu.adapter.DeleteCategoryAdapter
import com.example.dopaminemenu.vibemenu.viewmodel.MainViewModel
import com.google.firebase.database.database

class DeleteCategoryActivity : AppCompatActivity() {

    private lateinit var binding: DelcatrecyclerviewBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: DeleteCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DelcatrecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.heading.text = "Delete Category"

        adapter = DeleteCategoryAdapter(mutableListOf())
        binding.itemRv.layoutManager = LinearLayoutManager(this)
        binding.itemRv.adapter = adapter
        val database = com.google.firebase.Firebase.database.reference
        val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val customCategories = mutableListOf<com.example.dopaminemenu.vibemenu.model.Category>()
            adapter = DeleteCategoryAdapter(customCategories)
            binding.itemRv.layoutManager = LinearLayoutManager(this)
            binding.itemRv.adapter = adapter

            database.child("users").child(userId).child("customCategories")
                .get()
                .addOnSuccessListener { snapshot ->
                    customCategories.clear()
                    for (child in snapshot.children) {
                        val category = child.getValue(com.example.dopaminemenu.vibemenu.model.Category::class.java)
                        if (category != null) {
                            customCategories.add(category)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
                .addOnFailureListener {
                }
        }



        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.nav_add -> {
                    startActivity(Intent(this, AddActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, UserProfile::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
