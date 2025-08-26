package com.example.dopaminemenu.vibemenu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dopaminemenu.vibemenu.model.Category
import com.example.dopaminemenu.vibemenu.repository.MainRepo

class MainViewModel : ViewModel() {
    private val repo = MainRepo()

    fun loadcategories(): LiveData<MutableList<Category>> {
        return repo.loadCategory()
    }
}