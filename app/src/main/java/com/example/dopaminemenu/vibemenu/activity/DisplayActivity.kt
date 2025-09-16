package com.example.dopaminemenu.vibemenu.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dopaminemenu.R
import com.example.dopaminemenu.databinding.DisplayActivityBinding
import com.example.dopaminemenu.vibemenu.adapter.ActivitiesAdapter
import com.example.dopaminemenu.vibemenu.model.ActivityState
import com.example.dopaminemenu.vibemenu.viewmodel.MainViewModel
import com.google.firebase.database.database

class DisplayActivity : AppCompatActivity() {

    private lateinit var binding: DisplayActivityBinding
    private lateinit var Pendingadapter: ActivitiesAdapter
    private lateinit var Completedadapter: ActivitiesAdapter
    private lateinit var viewModel: MainViewModel
    private val database = com.google.firebase.Firebase.database.reference

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DisplayActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.heading.text = intent.getStringExtra("categoryName")

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        Pendingadapter =  ActivitiesAdapter(mutableListOf(), database)
        Completedadapter = ActivitiesAdapter(mutableListOf(), database)

        binding.itemRv.layoutManager = LinearLayoutManager(this)
        binding.itemRv.adapter = Pendingadapter

        binding.completedRv.layoutManager = LinearLayoutManager(this)
        binding.completedRv.adapter = Completedadapter


        val categoryName = intent.getStringExtra("categoryName") ?: ""

        viewModel.loadActivities().observe(this) { activities ->

            val pendingList = activities.filter {
                it.state == ActivityState.pending && it.category?.name == categoryName
            }
            val completedList = activities.filter {
                it.state == ActivityState.completed && it.category?.name == categoryName
            }
            Pendingadapter.activities.clear()
            Pendingadapter.activities.addAll(pendingList)
            Pendingadapter.notifyDataSetChanged()
            Completedadapter.activities.clear()
            Completedadapter.activities.addAll(completedList)
            Completedadapter.notifyDataSetChanged()

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

