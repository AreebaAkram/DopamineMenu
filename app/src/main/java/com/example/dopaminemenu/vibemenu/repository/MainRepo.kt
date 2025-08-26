package com.example.dopaminemenu.vibemenu.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dopaminemenu.vibemenu.model.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainRepo() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun loadCategory(): LiveData<MutableList<Category>> {
        val mutableData = MutableLiveData<MutableList<Category>>()
        val ref = firebaseDatabase.getReference("Category")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listData = mutableListOf<Category>()
                for (data in snapshot.children) {
                    val item = data.getValue(Category::class.java)
                    item?.let { listData.add(it) }
                }
                mutableData.value = listData
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
        return mutableData
    }
}