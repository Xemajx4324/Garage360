package com.mexiti.garage360.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mexiti.garage360.model.WorkOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkOrderDao {


    @Query("SELECT * FROM work_orders ORDER BY id DESC")
    fun getAllWorkOrders(): Flow<List<WorkOrder>>

    @Query("SELECT * FROM work_orders WHERE id = :id")
    fun getWorkOrderById(id: Long): Flow<WorkOrder>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workOrder: WorkOrder)

    @Update
    suspend fun update(workOrder: WorkOrder)

    @Delete
    suspend fun delete(workOrder: WorkOrder)


    @Query("SELECT * FROM work_orders WHERE clientId = :clientId ORDER BY id DESC")
    fun getWorkOrdersByClient(clientId: Long): Flow<List<WorkOrder>>
}