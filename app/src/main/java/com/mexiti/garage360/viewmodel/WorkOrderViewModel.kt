package com.mexiti.garage360.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    var state by mutableStateOf(WorkOrderState())
        private set

    init {
        viewModelScope.launch {
            repository.getAllWorkOrders().collectLatest { orders ->
                _workOrderList.value = orders
            }
        }
        viewModelScope.launch {
            repository.getAllClients().collectLatest { clients ->
                state = state.copy(clients = clients)
            }
        }
    }

    // --- Funciones CRUD ---
    fun addWorkOrder(order: WorkOrder) = viewModelScope.launch { repository.insertWorkOrder(order) }
    fun updateWorkOrder(order: WorkOrder) = viewModelScope.launch { repository.updateWorkOrder(order) }
    fun deleteWorkOrder(order: WorkOrder) = viewModelScope.launch { repository.deleteWorkOrder(order) }

    // --- Funciones para Add/Edit ---
    fun onClientSelected(clientId: Long) { state = state.copy(clientId = clientId) }
    fun onVehicleMakeChange(make: String) { state = state.copy(vehicleMake = make) }
    fun onVehicleModelChange(model: String) { state = state.copy(vehicleModel = model) }
    fun onVehicleYearChange(year: String) { state = state.copy(vehicleYear = year.toIntOrNull() ?: 2024) }
    fun onLicensePlateChange(plate: String) { state = state.copy(licensePlate = plate) }
    fun onDescriptionChange(description: String) { state = state.copy(description = description) }

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
                        status = order.status
                    )
                }
            }
        }
    }

    fun resetWorkOrderState() {
        state = state.copy(
            clientId = 0, vehicleMake = "", vehicleModel = "", vehicleYear = 2024,
            licensePlate = "", vin = "", description = "", status = "Pendiente"
        )
    }
}