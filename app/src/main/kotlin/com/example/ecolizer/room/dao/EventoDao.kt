package com.example.ecolizer.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.ecolizer.room.entities.Evento

@Dao
interface EventoDao {
    @Insert
    suspend fun replace(evento: Evento)

    @Query("SELECT * FROM eventos WHERE estacionId = :estacionId ORDER BY fechaHora DESC")
    suspend fun getByEstacionId(estacionId: Int): List<Evento>
}