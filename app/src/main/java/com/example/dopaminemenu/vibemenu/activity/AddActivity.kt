package com.example.dopaminemenu.vibemenu.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dopaminemenu.R
import com.example.dopaminemenu.databinding.AddoneActivityBinding
import com.example.dopaminemenu.vibemenu.model.Activity
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class AddActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: AddoneActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddoneActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnAdd.setText("Add")


        database = Firebase.database.reference

        binding.btnAdd.setOnClickListener {
            val name = binding.edtTaskName.text.toString()
            val desc = binding.edtTaskDesc.text.toString()
            writeNewActivity(name, desc)
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


    private fun writeNewActivity(name: String, desc: String) {
        val activity = Activity(name, desc)

        database.child("activities").child(name).setValue(activity)
            .addOnSuccessListener {
                val intent = Intent(this, DisplayActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add activity", Toast.LENGTH_SHORT).show()
            }
    }
}

