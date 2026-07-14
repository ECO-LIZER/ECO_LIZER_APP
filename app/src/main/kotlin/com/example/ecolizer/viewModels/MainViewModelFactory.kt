package com.example.ecolizer.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecolizer.room.dao.FalloDao
import com.example.ecolizer.room.dao.MaterialDao

class MainViewModelFactory(
    private val falloDao: FalloDao,
    private val materialDao: MaterialDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(
                falloDao = falloDao,
                materialDao = materialDao
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}