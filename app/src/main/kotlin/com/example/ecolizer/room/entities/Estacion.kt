package com.example.ecolizer.room.entities 

@Entity(tableName = "estaciones")
data class Estacion(
    @PrimaryKey
    val estacionId: Int,
    val nombre: String,
    val latitud: Double,
    val longitud: Double,
    val estadoOperativo: String,
    val ultimoMantenimiento: Long
)