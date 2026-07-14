package com.example.ecolizer.room.entities 

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