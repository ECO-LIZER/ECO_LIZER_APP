package com.example.ecolizer.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "eventos",
    foreignKeys = [
        ForeignKey(
            entity = Estacion::class,
            parentColumns = ["estacionId"],
            childColumns = ["estacionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("estacionId")]
)
data class Evento(
    @PrimaryKey(autoGenerate = true)
    val eventoId: Int = 0,
    val estacionId: Int,
    val fechaHora: Long,
    val tipoEvento: String,
    val descripcion: String,
    val resolucion: String
)