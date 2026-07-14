package com.example.ecolizer.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecolizer.room.entities.Material

@Dao
interface MaterialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(material: Material)

    @Query("SELECT * FROM materiales")
    fun getItems(): LiveData<List<Material>>
}