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
import com.example.dopaminemenu.vibemenu.model.Activity
import com.example.dopaminemenu.vibemenu.model.ActivityState
import com.example.dopaminemenu.vibemenu.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DisplayActivity : AppCompatActivity() {

    private lateinit var binding: DisplayActivityBinding
    private lateinit var Pendingadapter: ActivitiesAdapter
    private lateinit var Completedadapter: ActivitiesAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var database: DatabaseReference

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DisplayActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val categoryName = intent.getStringExtra("categoryName") ?: ""
        binding.heading.text = categoryName

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            finish()
            return
        }

        database = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("activities")
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        Pendingadapter = ActivitiesAdapter(mutableListOf(), database)
        Completedadapter = ActivitiesAdapter(mutableListOf(), database)

        binding.itemRv.layoutManager = LinearLayoutManager(this)
        binding.itemRv.adapter = Pendingadapter

        binding.completedRv.layoutManager = LinearLayoutManager(this)
        binding.completedRv.adapter = Completedadapter

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val activities = mutableListOf<Activity>()
                for (activitySnap in snapshot.children) {
                    val activity = activitySnap.getValue(Activity::class.java)
                    if (activity != null) {
                        activities.add(activity)
                    }
                }

                val now = System.currentTimeMillis()
                val expired = mutableListOf<Activity>()

                for (activity in activities) {
                    if (activity.state == ActivityState.completed) {
                        val completedAt = activity.completedAt ?: 0L
                        val shouldDelete = (now - completedAt) >= (24 * 60 * 60 * 1000)
                        if (shouldDelete) {
                            expired.add(activity)
                            activity.name?.let {
                                database.child(it).removeValue()
                            }
                        }
                    }
                }

                val validActivities = activities - expired.toSet()

                val pendingList = validActivities.filter {
                    it.state == ActivityState.pending && it.category?.name == categoryName
                }
                val completedList = validActivities.filter {
                    it.state == ActivityState.completed && it.category?.name == categoryName
                }

                Pendingadapter.activities.clear()
                Pendingadapter.activities.addAll(pendingList)
                Pendingadapter.notifyDataSetChanged()

                Completedadapter.activities.clear()
                Completedadapter.activities.addAll(completedList)
                Completedadapter.notifyDataSetChanged()

                if (pendingList.isEmpty()) {
                    binding.nocontent.text = "No activities found"
                    binding.nocontent.visibility = android.view.View.VISIBLE
                } else {
                    binding.nocontent.visibility = android.view.View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

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
