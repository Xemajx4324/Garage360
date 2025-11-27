package com.mexiti.garage360.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "work_orders",
    foreignKeys = [
        ForeignKey(entity = Client::class, parentColumns = ["id"], childColumns = ["clientId"], onDelete = ForeignKey.CASCADE)
    ],

    indices = [Index(value = ["clientId"])]
)
data class WorkOrder(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientId: Long,
    val vehicleMake: String,
    val vehicleModel: String,
    val vehicleYear: Int,
    val licensePlate: String,
    val vin: String?,
    val description: String,
    val status: String,
    val isUrgent: Boolean,


    val totalCost: Double = 0.0,
    val deadlineDate: Long = 0L
)