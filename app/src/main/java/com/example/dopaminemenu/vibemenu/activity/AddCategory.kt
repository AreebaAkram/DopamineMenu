package com.example.dopaminemenu.vibemenu.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dopaminemenu.R
import com.example.dopaminemenu.databinding.AddcategoryBinding
import com.example.dopaminemenu.vibemenu.model.Category
import com.example.dopaminemenu.vibemenu.viewmodel.MainViewModel

class AddCategory: AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var edtCategoryName: EditText
    private lateinit var edtCategoryDesc: EditText
    private lateinit var edtCategoryTime: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var binding: AddcategoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddcategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.addH2.setText("Add Category")
        edtCategoryName = binding.edtcategoryName
        btnAddCategory = binding.addbtn
        edtCategoryDesc = binding.edtcategorydesc
        edtCategoryTime = binding.edttime

        binding.categoryname.text = "Name"
        binding.categorydesc.text = "Description"
        binding.categorytime.text = "Time"

        btnAddCategory.setOnClickListener {
            val name = edtCategoryName.text.toString().trim()
            val desc = edtCategoryDesc.text.toString().trim()
            val time = edtCategoryTime.text.toString().trim()

            if (name.isNotEmpty()) {
                val category = Category(name = name, desc = desc, time = time )
                viewModel.addCategory(category) { success ->
                    if (success) {
                        Toast.makeText(this, "Category added!", Toast.LENGTH_SHORT).show()
                        edtCategoryName.text.clear()
                        edtCategoryDesc.text.clear()
                        edtCategoryTime.text.clear()
                    } else {
                        Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Enter category name", Toast.LENGTH_SHORT).show()
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
}