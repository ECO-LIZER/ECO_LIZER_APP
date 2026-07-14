package com.example.ecolizer.room.entities 

@Entity(tableName = "fallos")
data class Fallo(
    @PrimaryKey
    val nombre: String,
    val activo: Boolean
)