package com.example.dopaminemenu.vibemenu.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding.itemRv.layoutManager = LinearLayoutManager(this)
        binding.itemRv.adapter = adapter

        viewModel.loadcategories().observe(this) { categories ->
            adapter.categories.clear()
            adapter.categories.addAll(categories)
            adapter.notifyDataSetChanged()
        }
    }
}

