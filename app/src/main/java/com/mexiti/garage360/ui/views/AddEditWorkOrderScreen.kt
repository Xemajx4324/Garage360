package com.mexiti.garage360.ui.views
import com.mexiti.garage360.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mexiti.garage360.model.WorkOrder
import com.mexiti.garage360.viewmodel.WorkOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWorkOrderScreen(
    navController: NavController,
    orderId: Long,
    viewModel: WorkOrderViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var isClientDropdownExpanded by remember { mutableStateOf(false) }
    val selectedClientName = state.clients.find { it.id == state.clientId }?.name ?: "Seleccione un cliente"

    LaunchedEffect(key1 = orderId) {
        if (orderId != 0L) {
            viewModel.getWorkOrderById(orderId)
        } else {
            viewModel.resetWorkOrderState()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetWorkOrderState()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (orderId == 0L) "Nueva Orden" else "Editar Orden") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    )
    { paddingValues ->

        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        )
        {Image(
            painter = painterResource( R.drawable.car_image),
            contentDescription = "Fondo de taller mecánico",
            contentScale = ContentScale.Crop,
            // Hacemos la imagen semi-transparente para que el texto sea legible
            modifier = Modifier.matchParentSize().alpha(0.1f)
        )
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            ExposedDropdownMenuBox(
                expanded = isClientDropdownExpanded,
                onExpandedChange = { isClientDropdownExpanded = !isClientDropdownExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedClientName,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cliente") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isClientDropdownExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = isClientDropdownExpanded,
                    onDismissRequest = { isClientDropdownExpanded = false }
                ) {
                    state.clients.forEach { client ->
                        DropdownMenuItem(
                            text = { Text(client.name) },
                            onClick = {
                                viewModel.onClientSelected(client.id)
                                isClientDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.vehicleMake,
                onValueChange = { viewModel.onVehicleMakeChange(it) },
                label = { Text("Marca del Vehículo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.vehicleModel,
                onValueChange = { viewModel.onVehicleModelChange(it) },
                label = { Text("Modelo") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = if(state.vehicleYear == 0 || state.vehicleYear == 2024) "" else state.vehicleYear.toString(),
                onValueChange = { viewModel.onVehicleYearChange(it) },
                label = { Text("Año") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = state.licensePlate,
                onValueChange = { viewModel.onLicensePlateChange(it) },
                label = { Text("Placas") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Servicio a Realizar") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val orderToSave = WorkOrder(
                        id = orderId,
                        clientId = state.clientId,
                        vehicleMake = state.vehicleMake,
                        vehicleModel = state.vehicleModel,
                        vehicleYear = state.vehicleYear,
                        licensePlate = state.licensePlate,
                        vin = state.vin,
                        description = state.description,
                        status = state.status
                    )

                    if (orderId == 0L) {
                        viewModel.addWorkOrder(orderToSave)
                    } else {
                        viewModel.updateWorkOrder(orderToSave)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (orderId == 0L) "Guardar Orden" else "Actualizar Orden")
            }
        } // Fin de Column
        } // Fin de Box
    } // Fin del lambda de Scaffold
// ... (Fin de AddEditWorkOrderScreen)
}