package com.example.ecolizer.room.dao 

@Dao
interface EstacionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(estacion: Estacion): Long

    @Query("SELECT * FROM estaciones")
    suspend fun getItems(): List<Estacion>

    @Query("SELECT * FROM estaciones WHERE estacionId = :id")
    suspend fun getById(id: Int): Estacion?
}