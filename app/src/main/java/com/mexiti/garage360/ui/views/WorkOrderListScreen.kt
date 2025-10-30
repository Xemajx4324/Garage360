package com.mexiti.garage360.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mexiti.garage360.model.WorkOrder
import com.mexiti.garage360.viewmodel.WorkOrderViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun WorkOrderListScreen(
    navController: NavController,
    viewModel: WorkOrderViewModel = hiltViewModel()
) {
    val workOrders by viewModel.workOrderList.collectAsState()


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(workOrders) { order ->
            val deleteAction = SwipeAction(
                icon = rememberVectorPainter(image = Icons.Default.Delete),
                background = Color.Red,
                onSwipe = { viewModel.deleteWorkOrder(order) }
            )
            SwipeableActionsBox(
                startActions = listOf(deleteAction),
                swipeThreshold = 100.dp
            ) {
                WorkOrderCard(workOrder = order) {
                    navController.navigate("addEditWorkOrder/${order.id}")
                }
            }
        }
    }
}

@Composable
fun WorkOrderCard(workOrder: WorkOrder, onClick: () -> Unit) {
    val statusColor = when (workOrder.status.lowercase()) {
        "pendiente" -> Color(0xFFF44336) // Rojo
        "en proceso" -> Color(0xFFFF9800) // Naranja
        "terminado" -> Color(0xFF4CAF50) // Verde
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(10.dp)
                    .background(statusColor)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Filled.DirectionsCar,
                contentDescription = "Vehículo",
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = "${workOrder.vehicleMake} ${workOrder.vehicleModel} (${workOrder.vehicleYear})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Text(
                    text = workOrder.licensePlate,
                    fontSize = 15.sp,
                    color = Color.DarkGray
                )
                Text(
                    text = workOrder.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = workOrder.status.uppercase(),
                fontWeight = FontWeight.Bold,
                color = statusColor,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}