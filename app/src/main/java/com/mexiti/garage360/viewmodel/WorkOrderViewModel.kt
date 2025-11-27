package com.mexiti.garage360.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.mexiti.garage360.model.Car
import com.mexiti.garage360.model.WorkOrder
import com.mexiti.garage360.repository.GarageRepository
import com.mexiti.garage360.state.WorkOrderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkOrderViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    private val _workOrderList = MutableStateFlow<List<WorkOrder>>(emptyList())
    val workOrderList = _workOrderList.asStateFlow()

    // --- Lista para el Catálogo de Coches ---
    private val _carCatalog = MutableStateFlow<List<Car>>(emptyList())
    val carCatalog = _carCatalog.asStateFlow()

    private val db = FirebaseFirestore.getInstance()

    var state by mutableStateOf(WorkOrderState())
        private set

    init {
        // Cargar Órdenes
        viewModelScope.launch {
            repository.getAllWorkOrders().collectLatest { orders ->
                _workOrderList.value = orders
            }
        }
        // Cargar Clientes
        viewModelScope.launch {
            repository.getAllClients().collectLatest { clients ->
                state = state.copy(clients = clients)
            }
        }
        // Cargar Catálogo
        fetchCarCatalog()
    }

    private fun fetchCarCatalog() {
        db.collection("CarCatalog")
            .addSnapshotListener { value, error ->
                if (error != null) return@addSnapshotListener
                val cars = mutableListOf<Car>()
                if (value != null) {
                    for (doc in value) {
                        val car = doc.toObject(Car::class.java).copy(idDoc = doc.id)
                        cars.add(car)
                    }
                }
                _carCatalog.value = cars
            }
    }

    // --- Funciones CRUD ---
    fun addWorkOrder(order: WorkOrder) = viewModelScope.launch { repository.insertWorkOrder(order) }
    fun updateWorkOrder(order: WorkOrder) = viewModelScope.launch { repository.updateWorkOrder(order) }
    fun deleteWorkOrder(order: WorkOrder) = viewModelScope.launch { repository.deleteWorkOrder(order) }

    // --- Funciones de UI ---
    fun onClientSelected(clientId: Long) { state = state.copy(clientId = clientId) }
    fun onVehicleMakeChange(make: String) { state = state.copy(vehicleMake = make) }
    fun onVehicleModelChange(model: String) { state = state.copy(vehicleModel = model) }
    fun onVehicleYearChange(year: String) { state = state.copy(vehicleYear = year.toIntOrNull() ?: 2024) }
    fun onLicensePlateChange(plate: String) { state = state.copy(licensePlate = plate) }
    fun onDescriptionChange(description: String) { state = state.copy(description = description) }
    fun onIsUrgentChange(isUrgent: Boolean) { state = state.copy(isUrgent = isUrgent) }



    fun onTotalCostChange(cost: String) {
        // Validación simple: permitir solo números y punto decimal
        if (cost.all { it.isDigit() || it == '.' }) {
            state = state.copy(totalCost = cost)
        }
    }

    fun onDeadlineDateChange(date: Long) {
        state = state.copy(deadlineDate = date)
    }

    // -----------------------------------------------------

    fun getWorkOrderById(id: Long) {
        viewModelScope.launch {
            repository.getWorkOrderById(id).collectLatest { order ->
                if (order != null) {
                    state = state.copy(
                        clientId = order.clientId,
                        vehicleMake = order.vehicleMake,
                        vehicleModel = order.vehicleModel,
                        vehicleYear = order.vehicleYear,
                        licensePlate = order.licensePlate,
                        vin = order.vin ?: "",
                        description = order.description,
                        status = order.status,
                        isUrgent = order.isUrgent,

                        totalCost = order.totalCost.toString(),
                        deadlineDate = order.deadlineDate
                    )
                }
            }
        }
    }

    fun resetWorkOrderState() {
        state = state.copy(
            clientId = 0, vehicleMake = "", vehicleModel = "", vehicleYear = 2024,
            licensePlate = "", vin = "", description = "", status = "Pendiente", isUrgent = false,


            totalCost = "",
            deadlineDate = 0L
        )
    }
}