package com.example.ecolizer.room.entities 

@Entity(tableName = "materiales")
data class Material(
    @PrimaryKey
    val materialId: Int,
    val nombre: String,
    val cantidadRequerida: Int,
    val equivalenciaTicket: String,
    val valorTicket: Int,
    val icono: String,
    val activo: Boolean
)