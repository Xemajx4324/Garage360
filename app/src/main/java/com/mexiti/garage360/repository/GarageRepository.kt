package com.mexiti.garage360.repository

import com.mexiti.garage360.model.Client
import com.mexiti.garage360.model.WorkOrder
import com.mexiti.garage360.room.ClientDao
import com.mexiti.garage360.room.WorkOrderDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GarageRepository @Inject constructor(
    private val clientDao: ClientDao,
    private val workOrderDao: WorkOrderDao
) {
    // Client Functions
    fun getAllClients(): Flow<List<Client>> = clientDao.getAllClients()
    fun getClientById(id: Long): Flow<Client?> = clientDao.getClientById(id)
    suspend fun insertClient(client: Client) = clientDao.insert(client)
    suspend fun updateClient(client: Client) = clientDao.update(client)
    suspend fun deleteClient(client: Client) = clientDao.delete(client)

    // WorkOrder Functions
    fun getAllWorkOrders(): Flow<List<WorkOrder>> = workOrderDao.getAllWorkOrders()
    fun getWorkOrderById(id: Long): Flow<WorkOrder?> = workOrderDao.getWorkOrderById(id)
    fun getWorkOrdersByClient(clientId: Long): Flow<List<WorkOrder>> = workOrderDao.getWorkOrdersByClient(clientId)
    suspend fun insertWorkOrder(workOrder: WorkOrder) = workOrderDao.insert(workOrder)
    suspend fun updateWorkOrder(workOrder: WorkOrder) = workOrderDao.update(workOrder)
    suspend fun deleteWorkOrder(workOrder: WorkOrder) = workOrderDao.delete(workOrder)
}