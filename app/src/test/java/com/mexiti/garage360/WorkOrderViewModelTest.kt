package com.mexiti.garage360

import com.mexiti.garage360.model.WorkOrder
import com.mexiti.garage360.repository.GarageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import com.mexiti.garage360.viewmodel.WorkOrderViewModel

@ExperimentalCoroutinesApi
class WorkOrderViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: GarageRepository
    private lateinit var viewModel: WorkOrderViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(GarageRepository::class.java)

        // Preparamos el mock para llamadas del init del ViewModel
        `when`(repository.getAllWorkOrders()).thenReturn(emptyFlow())
        `when`(repository.getAllClients()).thenReturn(emptyFlow())

        // Creación  ViewModel
        viewModel = WorkOrderViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addWorkOrder should call insertWorkOrder on repository`() = runTest {
        // Given
        val workOrder = WorkOrder(
            clientId = 1,
            vehicleMake = "Toyota",
            vehicleModel = "Corolla",
            vehicleYear = 2022,
            licensePlate = "ABC-123",
            description = "Oil change"
        )

        // When
        viewModel.addWorkOrder(workOrder)

        // Forzamos la ejecución de la corutina pendiente
        testDispatcher.scheduler.runCurrent()


        verify(repository).insertWorkOrder(workOrder)
    }
}