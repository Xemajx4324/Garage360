package com.mexiti.garage360.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mexiti.garage360.model.Client
import com.mexiti.garage360.model.WorkOrder

@Database(entities = [Client::class, WorkOrder::class], version = 1, exportSchema = false)
abstract class GarageDatabase : RoomDatabase() {
    abstract fun clientDao(): ClientDao
    abstract fun workOrderDao(): WorkOrderDao
}