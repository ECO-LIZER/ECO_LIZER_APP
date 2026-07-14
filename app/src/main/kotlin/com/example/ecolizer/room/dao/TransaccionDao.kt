package com.example.ecolizer.room.dao

import androidx.room.Dao
import androidx.room.Insert
import com.example.ecolizer.room.entities.Transaccion

@Dao
interface TransaccionDao {
    @Insert
    suspend fun replace(transaccion: Transaccion)
}