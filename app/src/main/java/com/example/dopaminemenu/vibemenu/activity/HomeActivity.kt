package com.example.dopaminemenu.vibemenu.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dopaminemenu.R
import com.example.dopaminemenu.databinding.HomeviewBinding
import com.example.dopaminemenu.vibemenu.adapter.MainAdapter
import com.example.dopaminemenu.vibemenu.viewmodel.MainViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: HomeviewBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        adapter = MainAdapter(mutableListOf())
        { category ->
            val intent = Intent(this, DisplayActivity::class.java)
            intent.putExtra("categoryName", category.name)
            startActivity(intent)
        }
        binding.itemRv.adapter = adapter
        binding.itemRv.layoutManager = LinearLayoutManager(this)
        binding.itemRv.adapter = adapter

        viewModel.loadcategories().observe(this) { categories ->
            adapter.categories.clear()
            adapter.categories.addAll(categories)
            adapter.notifyDataSetChanged()
        }


        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "home icon clicked", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_add -> {
                    val intent = Intent(this, AddActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_profile -> {
                    true
                }
                else -> false
            }
        }
    }
}

