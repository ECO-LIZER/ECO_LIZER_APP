package com.example.ecolizer.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transacciones",
    foreignKeys = [
        ForeignKey(
            entity = Estacion::class,
            parentColumns = ["estacionId"],
            childColumns = ["estacionId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Material::class,
            parentColumns = ["materialId"],
            childColumns = ["materialId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("estacionId"), Index("materialId")]
)
data class Transaccion(
    @PrimaryKey(autoGenerate = true)
    val transaccionId: Int = 0,
    val estacionId: Int,
    val materialId: Int,
    val fecha: Long,
    val estado: String,
    val hashTicket: String,
    val hashSeguridad: String,
    val usado: Boolean
)