package com.example.dopaminemenu.vibemenu.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dopaminemenu.vibemenu.model.Category
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainRepo {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    fun loadCategory(): LiveData<MutableList<Category>> {
        val mutableData = MutableLiveData<MutableList<Category>>()
        val combinedList = mutableListOf<Category>()

        val globalRef = firebaseDatabase.getReference("Category")
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userRef = firebaseDatabase.getReference("users").child(userId ?: "").child("customCategories")

        globalRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val category = data.getValue(Category::class.java)
                    category?.let { combinedList.add(it) }
                }
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(userSnapshot: DataSnapshot) {
                        for (data in userSnapshot.children) {
                            val category = data.getValue(Category::class.java)
                            category?.let { combinedList.add(it) }
                        }

                        mutableData.value = combinedList
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        return mutableData
    }

    fun addCategory(category: Category, onResult: (Boolean) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            onResult(false)
            return
        }

        val ref = firebaseDatabase.getReference("users").child(userId).child("customCategories")
        val id = ref.push().key
        if (id != null) {
            ref.child(id).setValue(category)
                .addOnSuccessListener { onResult(true) }
                .addOnFailureListener { onResult(false) }
        } else {
            onResult(false)
        }
    }
}
