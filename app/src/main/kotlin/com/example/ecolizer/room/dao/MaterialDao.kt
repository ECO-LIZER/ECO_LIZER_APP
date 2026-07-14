package com.example.ecolizer.room.dao 

@Dao
interface MaterialDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replace(material: Material)

    @Query("SELECT * FROM materiales")
    fun getItems(): LiveData<List<Material>>
}