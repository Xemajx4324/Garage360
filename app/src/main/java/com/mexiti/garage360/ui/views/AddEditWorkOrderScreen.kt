package com.mexiti.garage360.ui.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mexiti.garage360.model.WorkOrder
import com.mexiti.garage360.ui.components.CarSelector
import com.mexiti.garage360.viewmodel.WorkOrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditWorkOrderScreen(
    navController: NavController,
    orderId: Long,
    viewModel: WorkOrderViewModel = hiltViewModel()
) {
    val state = viewModel.state

    // Obtenemos la lista de coches de Firebase desde el ViewModel
    val carList by viewModel.carCatalog.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    // Guardamos la fecha seleccionada (en milisegundos)
                    datePickerState.selectedDateMillis?.let { millis ->
                        viewModel.onDeadlineDateChange(millis)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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
        onDispose { viewModel.resetWorkOrderState() }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (orderId == 0L) "Nueva Orden" else "Editar Orden") },
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
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // --- Selector de Cliente ---
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
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isClientDropdownExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
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

            Spacer(modifier = Modifier.height(16.dp))

            // --- Carrusel de Coches (Firebase) ---
            CarSelector(
                carList = carList,
                onCarSelected = { make, model ->
                    // Al seleccionar un coche, llenamos los campos automáticamente
                    viewModel.onVehicleMakeChange(make)
                    viewModel.onVehicleModelChange(model)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // --- Datos del Vehículo ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = state.vehicleMake,
                    onValueChange = { viewModel.onVehicleMakeChange(it) },
                    label = { Text("Marca") },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = state.vehicleModel,
                    onValueChange = { viewModel.onVehicleModelChange(it) },
                    label = { Text("Modelo") },
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = if(state.vehicleYear == 0 || state.vehicleYear == 2024) "" else state.vehicleYear.toString(),
                    onValueChange = { viewModel.onVehicleYearChange(it) },
                    label = { Text("Año") },
                    modifier = Modifier.weight(0.4f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = MaterialTheme.shapes.medium
                )
                OutlinedTextField(
                    value = state.licensePlate,
                    onValueChange = { viewModel.onLicensePlateChange(it) },
                    label = { Text("Placas") },
                    modifier = Modifier.weight(0.6f),
                    shape = MaterialTheme.shapes.medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Descripción del Servicio ---
            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.onDescriptionChange(it) },
                label = { Text("Servicio a Realizar") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 4,
                shape = MaterialTheme.shapes.medium
            )

            // --- Checkbox Urgente ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                Checkbox(
                    checked = state.isUrgent,
                    onCheckedChange = { viewModel.onIsUrgentChange(it) }
                )
                Text("¿Es reparación urgente?")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Text("Facturación y Entrega", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. CAMPO DE COSTO ($)
                OutlinedTextField(
                    value = state.totalCost,
                    onValueChange = { viewModel.onTotalCostChange(it) },
                    label = { Text("Costo Total ($)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = MaterialTheme.shapes.medium,
                    leadingIcon = { Text("$", fontWeight = FontWeight.Bold) } // Signo de pesos
                )

                // 2. CAMPO DE FECHA (Solo lectura, abre el calendario)
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = if (state.deadlineDate == 0L) "" else convertMillisToDate(state.deadlineDate),
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Fecha Entrega") },
                        modifier = Modifier.fillMaxWidth(), // Llenamos el Box padre
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        trailingIcon = {
                            Icon(Icons.Default.DateRange, contentDescription = "Calendario")
                        },
                        shape = MaterialTheme.shapes.medium
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showDatePicker = true }
                    )
                }
            }



            // --- Botón Guardar ---
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
                        status = state.status,
                        isUrgent = state.isUrgent,
                        totalCost = state.totalCost.toDoubleOrNull() ?: 0.0,
                        deadlineDate = state.deadlineDate
                    )
                    if (orderId == 0L) viewModel.addWorkOrder(orderToSave) else viewModel.updateWorkOrder(orderToSave)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(if (orderId == 0L) "Crear Orden" else "Actualizar Orden", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    return formatter.format(java.util.Date(millis))
}