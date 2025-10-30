package com.mexiti.garage360.ui.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mexiti.garage360.model.Client
import com.mexiti.garage360.viewmodel.ClientViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditClientScreen(
    navController: NavController,
    clientId: Long,
    viewModel: ClientViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(key1 = clientId) {
        if (clientId != 0L) {
            viewModel.getClientById(clientId)
        } else {
            viewModel.resetClientState()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetClientState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (clientId == 0L) "Nuevo Cliente" else "Editar Cliente") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.onNameChange(it) },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.phone,

                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() } && newValue.length <= 10) {
                        viewModel.onPhoneChange(newValue)
                    }
                },
                label = { Text("Teléfono (10 dígitos)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.onEmailChange(it) },
                label = { Text("Correo Electrónico (Opcional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val clientToSave = Client(
                        id = clientId,
                        name = state.name,
                        phone = state.phone,
                        email = state.email.takeIf { it.isNotBlank() }
                    )
                    if (clientId == 0L) {
                        viewModel.addClient(clientToSave)
                    } else {
                        viewModel.updateClient(clientToSave)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (clientId == 0L) "Guardar Cliente" else "Actualizar Cliente")
            }
        }
    }
}