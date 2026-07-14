package com.example.ecolizer.room.dao 

@Dao
interface TransaccionDao {
    @Insert
    suspend fun replace(transaccion: Transaccion)
}