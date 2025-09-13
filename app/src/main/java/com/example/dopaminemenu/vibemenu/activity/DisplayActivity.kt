package com.example.dopaminemenu.vibemenu.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dopaminemenu.R
import com.example.dopaminemenu.databinding.DisplayActivityBinding
import com.example.dopaminemenu.vibemenu.adapter.ActivitiesAdapter
import com.example.dopaminemenu.vibemenu.adapter.MainAdapter
import com.example.dopaminemenu.vibemenu.viewmodel.MainViewModel

class DisplayActivity : AppCompatActivity() {

    private lateinit var binding: DisplayActivityBinding
    private lateinit var adapter: ActivitiesAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DisplayActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        adapter =  ActivitiesAdapter(mutableListOf())

        binding.itemRv.adapter = adapter
        binding.itemRv.layoutManager = LinearLayoutManager(this)
        binding.itemRv.adapter = adapter

        viewModel.loadActivities().observe(this) { activities ->
            adapter.activities.clear()
            adapter.activities.addAll(activities)
            adapter.notifyDataSetChanged()
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
                    true
                }
                else -> false
            }
        }
    }
}

