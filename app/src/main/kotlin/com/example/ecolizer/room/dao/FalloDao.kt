package com.example.ecolizer.room.dao 

@Dao
interface FalloDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(fallo: Fallo): Long

    @Query("SELECT * FROM fallos")
    fun getItems(): LiveData<List<Fallo>>
}