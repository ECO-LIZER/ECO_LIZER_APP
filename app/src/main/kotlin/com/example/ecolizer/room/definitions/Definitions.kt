package com.example.ecolizer.room.definitions

enum class EstacionesDef(val id: Int, val nombre: String, val latitud: Double, val longitud: Double, val estadoOperativo: EstadoOperativoDef, val ultimoMantenimiento: Long) {
    TERMINAL_CENTRAL(
        id = 1,
        nombre = "Terminal Central",
        latitud = 19.4326,
        longitud = -99.1332,
        estadoOperativo = EstadoOperativoDef.ACTIVO,
        ultimoMantenimiento = System.currentTimeMillis()
    ),
    PLAZA_MAYOR(
        id = 2,
        nombre = "Plaza Mayor",
        latitud = 19.4902,
        longitud = -99.1436,
        estadoOperativo = EstadoOperativoDef.INACTIVO,
        ultimoMantenimiento = System.currentTimeMillis()
    ),
    DISTRITO_TECNOLOGICO(
        id =  3,
        nombre = "Distrito Tecnológico",
        latitud = 19.3512,
        longitud = -99.1627,
        estadoOperativo = EstadoOperativoDef.INACTIVO,
        ultimoMantenimiento = System.currentTimeMillis()
    ),
    PARQUE_CENTENARIO(
        id = 4,
        nombre = "Parque Centenario",
        latitud = 19.3607,
        longitud = -99.2736,
        estadoOperativo = EstadoOperativoDef.ACTIVO,
        ultimoMantenimiento = System.currentTimeMillis()
    ),
    BAHIA_NORTE(
        id = 5,
        nombre = "Bahía Norte",
        latitud = 19.4361,
        longitud = -99.0719,
        estadoOperativo = EstadoOperativoDef.MANTENIMIENTO,
        ultimoMantenimiento = System.currentTimeMillis()
    ),
    CORREDOR_INDUSTRIAL(
        id = 6,
        nombre = "Corredor Industrial",
        latitud = 19.4270,
        longitud = -99.1677,
        estadoOperativo = EstadoOperativoDef.ACTIVO,
        ultimoMantenimiento = System.currentTimeMillis()
    ),
    ALTOS_DEL_SOL(
        id = 7,
        nombre = "Altos del Sol",
        latitud = 19.5105,
        longitud = -99.2348,
        estadoOperativo = EstadoOperativoDef.MANTENIMIENTO,
        ultimoMantenimiento = System.currentTimeMillis()
    ),
    PUENTE_VIEJO(
        id = 8,
        nombre = "Puente Viejo",
        latitud = 19.2890,
        longitud = -99.1693,
        estadoOperativo = EstadoOperativoDef.CONTENEDOR_LLENO,
        ultimoMantenimiento = System.currentTimeMillis()
    ),
    ZONA_FRANCA(
        id = 9,
        nombre = "Zona Franca",
        latitud = 19.4623,
        longitud = -99.0689,
        estadoOperativo = EstadoOperativoDef.CONTENEDOR_LLENO,
        ultimoMantenimiento = System.currentTimeMillis()
    ),
    MIRADOR_DEL_VALLE(
        id = 10,
        nombre = "Mirador del Valle",
        latitud = 19.7121,
        longitud = -98.9744,
        estadoOperativo = EstadoOperativoDef.ACTIVO,
        ultimoMantenimiento = System.currentTimeMillis()
    )
}

enum class EstadoOperativoDef {
    ACTIVO,
    INACTIVO,
    MANTENIMIENTO,
    CONTENEDOR_LLENO
}

enum class FallosDef(val activo: Boolean) {
    EVENTO_CONTENEDOR_LLENO(activo = false),
    EVENTO_MANTENIMIENTO(activo = false),
    EVENTO_FUERA_SERVICIO(activo = false),
    ERROR_OBJETO_INCOMPATIBLE(activo = false),
    ERROR_TRANSACCION_FALLIDA(activo = false),
    FALLO_TRANSACCION(activo = false),
    FALLO_NO_DETECTADO(activo = false),
    FALLO_SISTEMA(activo = false),
    FALLO_DESCONOCIDO(activo = false)
}

enum class MaterialesDef(val id: Int, val nombre: String, val cantidadRequerida: Int, val equivalenciaTicket: String, val valorTicket: Int, val icono: String, val activo: Boolean) {
    PET(
        id = 1,
        nombre = "PET",
        cantidadRequerida = 3,
        equivalenciaTicket = "3 Botellas",
        valorTicket = 1,
        icono = "botella.png",
        activo = true
    ),
    TETRA(
        id = 2,
        nombre = "TETRA",
        cantidadRequerida = 5,
        equivalenciaTicket = "5 Envases",
        valorTicket = 1,
        icono = "tetrapack.png",
        activo = true
    ),
    LATA(
        id = 3,
        nombre = "ALUMINIO",
        cantidadRequerida = 6,
        equivalenciaTicket = "6 Latas",
        valorTicket = 1,
        icono = "lata.png",
        activo = true
    );

    companion object {
        fun from(value: String): MaterialesDef {
            if(value == PET.nombre) {
                return PET
            }

            if(value == TETRA.nombre) {
                return TETRA
            }

            if(value == LATA.nombre) {
                return LATA
            }

            return PET
        }
    }
}

enum class EstadoTransaccion {
    EXITOSO,
    FALLO_TRANSACCION,
    FALLO_IMPRESORA,
    FALLO_SISTEMA,
    FALLO_DESCONOCIDO
}

enum class TipoEvento {
    CONTENEDOR_LLENO,
    MANTENIMIENTO,
    FUERA_SERVICIO
}

enum class ResolucionEvento {
    PENDIENTE,
    RESUELTO
}

enum class EventosDef(val estacionId: Int, val tipoEvento: TipoEvento, val descripcion: String, val resolucion: String) {
    CONTENEDOR_LLENO(
        estacionId = EstacionesDef.PARQUE_CENTENARIO.id,
        descripcion = "El contenedor está lleno",
        tipoEvento = TipoEvento.CONTENEDOR_LLENO,
        resolucion = ResolucionEvento.PENDIENTE.name
    ),
    MANTENIMIENTO(
        estacionId = EstacionesDef.PARQUE_CENTENARIO.id,
        descripcion = "El contenedor está en mantenimiento",
        tipoEvento = TipoEvento.MANTENIMIENTO,
        resolucion = ResolucionEvento.PENDIENTE.name
    ),
    FUERA_SERVICIO(
        estacionId = EstacionesDef.PARQUE_CENTENARIO.id,
        descripcion = "El contenedor está fuera de servicio",
        tipoEvento = TipoEvento.FUERA_SERVICIO,
        resolucion = ResolucionEvento.PENDIENTE.name
    )
}