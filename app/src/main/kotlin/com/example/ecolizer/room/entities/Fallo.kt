package com.example.ecolizer.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fallos")
data class Fallo(
    @PrimaryKey
    val nombre: String,
    val activo: Boolean
)