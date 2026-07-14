package com.example.ecolizer.room.definitions

import com.example.ecolizer.room.entities.Estacion
import com.example.ecolizer.room.entities.Evento
import com.example.ecolizer.room.entities.Fallo
import com.example.ecolizer.room.entities.Material

fun EstacionesDef.toEstacion(): Estacion {
    return Estacion(
        estacionId = this.id,
        nombre = this.nombre,
        latitud = this.latitud,
        longitud = this.longitud,
        estadoOperativo = this.estadoOperativo.name,
        ultimoMantenimiento = this.ultimoMantenimiento
    )
}

fun FallosDef.toFallo(): Fallo {
    return Fallo(
        nombre = this.name,
        activo = this.activo
    )
}

fun MaterialesDef.toMaterial(): Material {
    return Material(
        materialId = this.id,
        nombre = this.nombre,
        cantidadRequerida = this.cantidadRequerida,
        equivalenciaTicket = this.equivalenciaTicket,
        valorTicket = this.valorTicket,
        icono = this.icono,
        activo = this.activo
    )
}

fun EventosDef.toEvento(): Evento {
    return Evento(
        estacionId = this.estacionId,
        fechaHora = System.currentTimeMillis(),
        tipoEvento = this.tipoEvento.name,
        descripcion = this.descripcion,
        resolucion = this.resolucion
    )
}