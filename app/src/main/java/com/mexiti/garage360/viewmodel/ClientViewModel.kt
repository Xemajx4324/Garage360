package com.mexiti.garage360.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mexiti.garage360.model.Client
import com.mexiti.garage360.repository.GarageRepository
import com.mexiti.garage360.state.ClientState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientViewModel @Inject constructor(
    private val repository: GarageRepository
) : ViewModel() {

    // --- Para la LISTA de Clientes (Home) ---
    private val _clientList = MutableStateFlow<List<Client>>(emptyList())
    val clientList = _clientList.asStateFlow()

    // --- Para el ESTADO de la pantalla de Edición/Añadir ---
    var state by mutableStateOf(ClientState())
        private set

    init {
        // Carga la lista de clientes al iniciar
        viewModelScope.launch {
            repository.getAllClients().collectLatest { clients ->
                _clientList.value = clients
            }
        }
    }

    // --- Funciones CRUD para la Base de Datos ---
    fun addClient(client: Client) = viewModelScope.launch {
        repository.insertClient(client)
    }

    fun updateClient(client: Client) = viewModelScope.launch {
        repository.updateClient(client)
    }

    fun deleteClient(client: Client) = viewModelScope.launch {
        repository.deleteClient(client)
    }

    // --- Funciones para la pantalla de Edición/Añadir ---

    // Carga los datos del cliente en el 'state'
    fun getClientById(id: Long) {
        viewModelScope.launch {
            repository.getClientById(id).collectLatest { client ->
                if (client != null) {
                    state = state.copy(
                        name = client.name,
                        phone = client.phone,
                        email = client.email ?: ""
                    )
                }
            }
        }
    }

    // Limpia el 'state' para un nuevo cliente
    fun resetClientState() {
        state = ClientState()
    }

    // Actualiza el 'state' conforme el usuario escribe
    fun onNameChange(name: String) {
        state = state.copy(name = name)
    }
    fun onPhoneChange(phone: String) {
        state = state.copy(phone = phone)
    }
    fun onEmailChange(email: String) {
        state = state.copy(email = email)
    }
}