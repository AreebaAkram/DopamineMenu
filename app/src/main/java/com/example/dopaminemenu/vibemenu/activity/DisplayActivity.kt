package com.example.dopaminemenu.vibemenu.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dopaminemenu.databinding.DisplayActivityBinding

class DisplayActivity : AppCompatActivity() {

    private lateinit var binding: DisplayActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DisplayActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}