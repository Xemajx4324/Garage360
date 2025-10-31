package com.mexiti.garage360

import com.mexiti.garage360.model.Client
import com.mexiti.garage360.repository.GarageRepository
import com.mexiti.garage360.viewmodel.ClientViewModel
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

@ExperimentalCoroutinesApi
class ClientViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: GarageRepository
    private lateinit var viewModel: ClientViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mock(GarageRepository::class.java)

        `when`(repository.getAllClients()).thenReturn(emptyFlow())

        viewModel = ClientViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `addClient should call insertClient on repository`() = runTest {
        // Given
        val client = Client(name = "John Doe", phone = "1234567890", email = "john.doe@example.com")

        // When
        viewModel.addClient(client)

        // --- LA LÍNEA MÁGICA ---
        // Forzamos la ejecución de la corutina que acabamos de lanzar.
        testDispatcher.scheduler.runCurrent()

        // Then
        // Ahora la verificación funcionará porque la tarea pendiente ya se ejecutó.
        verify(repository).insertClient(client)
    }
}