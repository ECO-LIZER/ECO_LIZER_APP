package com.example.ecolizer.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ecolizer.room.dao.FalloDao
import com.example.ecolizer.room.dao.MaterialDao
import com.example.ecolizer.room.entities.Fallo
import com.example.ecolizer.room.entities.Material

class MainViewModel(
    falloDao: FalloDao,
    materialDao: MaterialDao,
): ViewModel() {
    val fallos: LiveData<List<Fallo>> = falloDao.getItems()
    val materiales: LiveData<List<Material>> = materialDao.getItems()
}