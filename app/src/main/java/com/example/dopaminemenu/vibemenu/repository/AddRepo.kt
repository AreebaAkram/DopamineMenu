package com.example.dopaminemenu.vibemenu.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dopaminemenu.vibemenu.model.Activity
import com.example.dopaminemenu.vibemenu.model.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddRepo() {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun loadActivities(): LiveData<MutableList<Activity>> {
        val mutableData = MutableLiveData<MutableList<Activity>>()
        val ref = firebaseDatabase.getReference("activities")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listData = mutableListOf<Activity>()
                for (data in snapshot.children) {
                    val item = data.getValue(Activity::class.java)
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