package com.mexiti.garage360.room

import androidx.room.*
import com.mexiti.garage360.model.WorkOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkOrderDao {
    // Ordena por fecha de entrada, las más nuevas primero
    @Query("SELECT * FROM work_orders ORDER BY entryDate DESC")
    fun getAllWorkOrders(): Flow<List<WorkOrder>>

    @Query("SELECT * FROM work_orders WHERE id = :id")
    fun getWorkOrderById(id: Long): Flow<WorkOrder?>

    // Para ver todas las órdenes de un cliente específico
    @Query("SELECT * FROM work_orders WHERE clientId = :clientId ORDER BY entryDate DESC")
    fun getWorkOrdersByClient(clientId: Long): Flow<List<WorkOrder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workOrder: WorkOrder)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(workOrder: WorkOrder)

    @Delete
    suspend fun delete(workOrder: WorkOrder)
}