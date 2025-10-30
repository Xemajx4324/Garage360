package com.mexiti.garage360.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mexiti.garage360.model.Client
import com.mexiti.garage360.viewmodel.ClientViewModel
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun ClientListScreen(
    navController: NavController,
    viewModel: ClientViewModel = hiltViewModel()
) {
    val clients by viewModel.clientList.collectAsState()


    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(clients) { client ->
            val deleteAction = SwipeAction(
                icon = rememberVectorPainter(image = Icons.Default.Delete),
                background = Color.Red,
                onSwipe = { viewModel.deleteClient(client) }
            )
            SwipeableActionsBox(
                startActions = listOf(deleteAction),
                swipeThreshold = 100.dp
            ) {
                ClientCard(client = client) {
                    navController.navigate("addEditClient/${client.id}")
                }
            }
        }
    }
}


@Composable
fun ClientCard(client: Client, onClick: () -> Unit) {

}