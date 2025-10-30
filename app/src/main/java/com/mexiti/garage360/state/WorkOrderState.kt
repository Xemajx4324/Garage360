package com.mexiti.garage360.state

import com.mexiti.garage360.model.Client

data class WorkOrderState(
    val clients: List<Client> = emptyList(),
    val clientId: Long = 0,
    val vehicleMake: String = "",
    val vehicleModel: String = "",
    val vehicleYear: Int = 2024,
    val licensePlate: String = "",
    val vin: String = "",
    val description: String = "",
    val status: String = "Pendiente"
)