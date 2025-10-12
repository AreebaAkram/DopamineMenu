package com.example.dopaminemenu.vibemenu.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dopaminemenu.vibemenu.model.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AddRepo {
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun loadActivities(): LiveData<MutableList<Activity>> {
        val mutableData = MutableLiveData<MutableList<Activity>>()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId == null) {
            mutableData.value = mutableListOf()
            return mutableData
        }

        val ref = firebaseDatabase.getReference("users").child(userId).child("activities")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listData = mutableListOf<Activity>()
                for (data in snapshot.children) {
                    val item = data.getValue(Activity::class.java)
                    item?.let { listData.add(it) }
                }
                mutableData.value = listData
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return mutableData
    }
}
