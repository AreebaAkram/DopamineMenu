package com.example.dopaminemenu.vibemenu.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dopaminemenu.R
import com.example.dopaminemenu.databinding.AddoneActivityBinding

class AddActivity : AppCompatActivity() {

    private lateinit var  binding : AddoneActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddoneActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setText("Add")
    }
}