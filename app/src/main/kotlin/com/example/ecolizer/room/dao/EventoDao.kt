package com.example.ecolizer.room.dao 

@Dao
interface EventoDao {
    @Insert
    suspend fun replace(evento: Evento)

    @Query("SELECT * FROM eventos WHERE estacionId = :estacionId ORDER BY fechaHora DESC")
    suspend fun getByEstacionId(estacionId: Int): List<Evento>
}