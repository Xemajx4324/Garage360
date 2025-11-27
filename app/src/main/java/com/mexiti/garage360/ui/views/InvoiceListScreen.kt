package com.mexiti.garage360.ui.views

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mexiti.garage360.viewmodel.WorkOrderViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun InvoiceListScreen(
    navController: NavController,
    viewModel: WorkOrderViewModel = hiltViewModel()
) {
    val orders by viewModel.workOrderList.collectAsState()
    val context = LocalContext.current

    // Filtramos solo las órdenes que tengan costo > 0 (asumimos que esas son facturables)
    val invoiceableOrders = orders.filter { it.totalCost > 0 }

    Scaffold(
        containerColor = Color(0xFFF5F5F5) // Un gris muy clarito de fondo
    ) { padding ->
        if (invoiceableOrders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No hay facturas pendientes", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(invoiceableOrders) { order ->
                    InvoiceCard(
                        order = order,
                        onSendEmail = {
                            // Aquí llamamos a la función de enviar correo
                            sendInvoiceEmail(context, order)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InvoiceCard(order: com.mexiti.garage360.model.WorkOrder, onSendEmail: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${order.vehicleMake} ${order.vehicleModel}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = "Fecha: ${formatDate(order.deadlineDate)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "Cliente ID: ${order.clientId}", // Idealmente aquí buscarías el nombre del cliente
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${order.totalCost}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                IconButton(onClick = onSendEmail) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Enviar Correo",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

// Función auxiliar para formatear fecha
fun formatDate(millis: Long): String {
    if (millis == 0L) return "Sin fecha"
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

// --- Lógica para enviar el Correo ---
fun sendInvoiceEmail(context: Context, order: com.mexiti.garage360.model.WorkOrder) {
    val subject = "Factura Garage 360 - ${order.vehicleMake} ${order.vehicleModel}"
    val body = """
        Hola,
        
        Adjunto los detalles de su servicio:
        
        Vehículo: ${order.vehicleMake} ${order.vehicleModel}
        Servicio: ${order.description}
        Total a pagar: $${order.totalCost}
        
        Gracias por su preferencia.
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "message/rfc822" // Esto fuerza a que se abra una app de correo
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
        // putExtra(Intent.EXTRA_EMAIL, arrayOf("cliente@email.com")) // Aquí podrías poner el email del cliente si lo tienes
    }

    try {
        context.startActivity(Intent.createChooser(intent, "Enviar factura por..."))
    } catch (e: Exception) {
        // Manejar caso donde no haya app de correo
    }
}