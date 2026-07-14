package com.example.ecolizer.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecolizer.room.entities.Fallo

@Dao
interface FalloDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(fallo: Fallo): Long

    @Query("SELECT * FROM fallos")
    fun getItems(): LiveData<List<Fallo>>
}