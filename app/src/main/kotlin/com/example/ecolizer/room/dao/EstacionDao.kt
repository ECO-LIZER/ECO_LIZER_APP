package com.example.ecolizer.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecolizer.room.entities.Estacion

@Dao
interface EstacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(estacion: Estacion): Long

    @Query("SELECT * FROM estaciones")
    suspend fun getItems(): List<Estacion>

    @Query("SELECT * FROM estaciones WHERE estacionId = :id")
    suspend fun getById(id: Int): Estacion?
}