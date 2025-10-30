package com.mexiti.garage360.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "work_orders",
    foreignKeys = [ForeignKey(entity = Client::class,
        parentColumns = ["id"],
        childColumns = ["clientId"],
        onDelete = ForeignKey.CASCADE)]) // Si borras un cliente, se borran sus órdenes
data class WorkOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val clientId: Long, // Para relacionarlo con el cliente
    val vehicleMake: String, // Marca
    val vehicleModel: String, // Modelo
    val vehicleYear: Int, // Año (Tipo Int)
    val licensePlate: String, // Placa
    val vin: String? = null, // VIN (Opcional)
    val description: String, // Servicio a realizar
    val entryDate: Long = System.currentTimeMillis(), // Fecha de entrada (Tipo Long)
    var status: String = "Pendiente" // Estado: Pendiente, En Proceso, Terminado (Tipo String)
    // Podrías añadir fecha de salida, costo, etc.
)