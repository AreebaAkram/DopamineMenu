package com.example.dopaminemenu.vibemenu.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dopaminemenu.R
import com.example.dopaminemenu.databinding.AddoneActivityBinding
import com.example.dopaminemenu.vibemenu.model.Activity
import com.example.dopaminemenu.vibemenu.model.ActivityState
import com.example.dopaminemenu.vibemenu.model.Category
import com.example.dopaminemenu.vibemenu.viewmodel.MainViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class AddActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: AddoneActivityBinding
    private lateinit var categorySpinner: Spinner
    private lateinit var viewmodel: MainViewModel

    private val categoryList = mutableListOf<Category>()
    private val categoryNames = mutableListOf<String>()
    private var selectedCategory: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddoneActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categorySpinner = binding.spinnerCategory
        categorySpinner.prompt = "Select Category"
        binding.btnAdd.text = "Add"
        binding.h1.text = "Add Activity"
        binding.taskName.text = "Activity"
        binding.taskDesc.text = "Description"
        binding.taskCategory.text = "Category"

        database = com.google.firebase.Firebase.database.reference
        viewmodel = ViewModelProvider(this)[MainViewModel::class.java]

        viewmodel.loadcategories().observe(this) { categories ->
            categoryList.clear()
            categoryList.addAll(categories)

            categoryNames.clear()
            categoryNames.addAll(categories.map { it.name ?: "" })

            val adapter = ArrayAdapter(this, R.layout.spinner_item, categoryNames)
            adapter.setDropDownViewResource(R.layout.spinner_dropdown)
            categorySpinner.adapter = adapter

            categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCategory = categoryList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    selectedCategory = null
                }
            }
        }


        binding.btnAdd.setOnClickListener {
            val name = binding.edtTaskName.text.toString()
            val desc = binding.edtTaskDesc.text.toString()
            val category = selectedCategory
            val state = ActivityState.pending

            if (name.isNotBlank() && desc.isNotBlank() && category != null) {
                writeNewActivity(name, desc, selectedCategory!!, state)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_add -> {
                    val intent = Intent(this, AddActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_profile -> {
                    val intent = Intent(this, UserProfile::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
    }
    private fun writeNewActivity(name: String, desc: String, category: Category, state: ActivityState = ActivityState.pending) {
        val activity = Activity(name, desc, category, state)

        database.child("activities").child(name).setValue(activity)
            .addOnSuccessListener {
                Toast.makeText(this, "Activity added successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DisplayActivity::class.java)
                intent.putExtra("categoryName", category.name)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add activity", Toast.LENGTH_SHORT).show()
            }
    }
}
