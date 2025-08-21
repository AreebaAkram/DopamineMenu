package com.example.dopaminemenu.vibemenu.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dopaminemenu.R
import com.example.dopaminemenu.databinding.HomeviewBinding
import com.example.dopaminemenu.vibemenu.model.Category
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class HomeActivity : AppCompatActivity() {

    var binding : HomeviewBinding? = null
    var adapter : CategoryAdapter? = null
    val categoryList = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.homeview)

        categoryList.addAll(arrayListOf(
            Category(
                name = "Entress",
                desc = "Quick wins for a burst of joy",
                time = "10-15 min"
            ),
            Category(
                name = "Main",
                desc = "Satisfying activities for deep fulfillment - Take a bit of time",
                time = "195 min +"
            ),
            Category(
                name = "Specials",
                desc = "Occasional activities - Cost time or money",
                time = "Big Events"
            ),
            Category(
                name = "Sides",
                desc = "Small additions for extra motivation - Add to boring tasks for more engagement",
                time = "Add to Boring Tasks"
            ),
            Category(
                name = "Desserts",
                desc = "Sweet treats for relaxation & joy - Enjoy sparingly",
                time = "Every Now & Then"
            )
        ))
        adapter = CategoryAdapter()
        binding?.itemRv?.layoutManager = LinearLayoutManager(this)
        binding?.itemRv?.adapter = adapter
        adapter?.submitList(categoryList)
        adapter?.notifyDataSetChanged()

    }
}
