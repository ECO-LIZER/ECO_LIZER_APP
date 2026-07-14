package com.example.ecolizer.room.database

import android.content.Context
import android.util.Log

@Database(
    entities = [Estacion::class, Fallo::class, Material::class, Transaccion::class, Evento::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun estacionDao(): EstacionDao
    abstract fun falloDao(): FalloDao
    abstract fun materialDao(): MaterialDao
    abstract fun transaccionDao(): TransaccionDao
    abstract fun eventoDao(): EventoDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                this.instance = Room.databaseBuilder(
                    context = context.applicationContext,
                    klass = AppDatabase::class.java,
                    name = "Ecolizer"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d("ROOM_DEBUG", "Base de datos CREADA desde cero (Datos perdidos)")

                        CoroutineScope(Dispatchers.IO).launch {
                            val database = getDatabase(context)
                            initEstaciones(database = database)
                            initFallos(database = database)
                            initMateriales(database = database)
                        }
                    }
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        Log.d("ROOM_DEBUG", "Base de datos ABIERTA (Datos persistidos)")
                    }
                }).build()

                this.instance!!
            }
        }

        private fun initEstaciones(database: AppDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                database.estacionDao().apply {
                    replace(EstacionesDef.TERMINAL_CENTRAL.toEstacion())
                    replace(EstacionesDef.PLAZA_MAYOR.toEstacion())
                    replace(EstacionesDef.DISTRITO_TECNOLOGICO.toEstacion())
                    replace(EstacionesDef.PARQUE_CENTENARIO.toEstacion())
                    replace(EstacionesDef.BAHIA_NORTE.toEstacion())
                    replace(EstacionesDef.CORREDOR_INDUSTRIAL.toEstacion())
                    replace(EstacionesDef.ALTOS_DEL_SOL.toEstacion())
                    replace(EstacionesDef.PUENTE_VIEJO.toEstacion())
                    replace(EstacionesDef.ZONA_FRANCA.toEstacion())
                    replace(EstacionesDef.MIRADOR_DEL_VALLE.toEstacion())
                }
            }
        }

        private fun initFallos(database: AppDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                database.falloDao().apply {
                    // Al comenzar
                    replace(FallosDef.EVENTO_CONTENEDOR_LLENO.toFallo())
                    replace(FallosDef.EVENTO_MANTENIMIENTO.toFallo())
                    replace(FallosDef.EVENTO_FUERA_SERVICIO.toFallo())

                    // Durante validación
                    replace(FallosDef.ERROR_OBJETO_INCOMPATIBLE.toFallo())
                    replace(FallosDef.ERROR_TRANSACCION_FALLIDA.toFallo())

                    // Pantalla success
                    replace(FallosDef.FALLO_TRANSACCION.toFallo())
                    replace(FallosDef.FALLO_NO_DETECTADO.toFallo())
                    replace(FallosDef.FALLO_SISTEMA.toFallo())
                    replace(FallosDef.FALLO_DESCONOCIDO.toFallo())
                }
            }
        }

        private fun initMateriales(database: AppDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                database.materialDao().apply {
                    replace(MaterialesDef.PET.toMaterial())
                    replace(MaterialesDef.TETRA.toMaterial())
                    replace(MaterialesDef.LATA.toMaterial())
                }
            }
        }
    }
}