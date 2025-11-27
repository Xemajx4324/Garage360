package com.mexiti.garage360.ui.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RequestQuote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mexiti.garage360.navigation.NavigationItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Estado del menú lateral
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Definimos las secciones de la app
    val navigationItems = listOf(
        NavigationItem("Clientes", Icons.Default.People, "clientList"),
        NavigationItem("Vehículos", Icons.Default.DirectionsCar, "workOrders"), // Pendiente de crear
        NavigationItem("Facturas", Icons.Default.RequestQuote, "invoices")      // Pendiente de crear
    )

    // Pantallas que muestran la barra principal
    val mainRoutes = listOf("clientList", "workOrders", "invoices")
    val showBars = currentRoute in mainRoutes

    val currentTitle = when (currentRoute) {
        "clientList" -> "Clientes"
        "workOrders" -> "Órdenes de Trabajo"
        "invoices" -> "Facturación"
        else -> "Garage 360"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Garage 360", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineSmall)
                HorizontalDivider()
                navigationItems.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                            scope.launch { drawerState.close() }
                        },
                        icon = { Icon(item.icon, contentDescription = null) }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (showBars) {
                    CenterAlignedTopAppBar(
                        title = { Text(currentTitle, fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menú")
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            },
            floatingActionButton = {
                if (showBars) {
                    FloatingActionButton(
                        onClick = {
                            // Lógica para el botón + según la pantalla
                            when (currentRoute) {
                                "clientList" -> navController.navigate("addEditClient/0")
                                "workOrders" -> navController.navigate("addEditWorkOrder/0")
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Agregar")
                    }
                }
            },
            bottomBar = {
                if (showBars) {
                    NavigationBar {
                        navigationItems.forEach { item ->
                            NavigationBarItem(
                                selected = currentRoute == item.route,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                },
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                label = { Text(item.label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            GarageNavHost(navController, innerPadding)
        }
    }
}

@Composable
fun HorizontalDivider() {
    // Cambiamos "HorizontalDivider" por "Divider"
    androidx.compose.material3.Divider(
        modifier = Modifier.padding(vertical = 8.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.outlineVariant
    )
}
@Composable
fun GarageNavHost(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "clientList",
        modifier = Modifier.padding(padding)
    ) {
        // --- CLIENTES ---
        composable("clientList") {
            ClientListScreen(navController = navController)
        }
        composable(
            "addEditClient/{clientId}",
            arguments = listOf(navArgument("clientId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("clientId") ?: 0L
            AddEditClientScreen(navController = navController, clientId = id)
        }



        // 1. Pantalla principal de Órdenes (Donde está el botón +)
        composable("workOrders") {
            // REEMPLAZA EL BOX QUE TENÍAS AQUÍ POR ESTO:
            WorkOrderListScreen(navController = navController)
        }

        // 2. Pantalla para AGREGAR/EDITAR (El Car Selector)
        composable(
            "addEditWorkOrder/{workOrderId}",
            arguments = listOf(navArgument("workOrderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("workOrderId") ?: 0L

            // ¡AQUÍ LLAMAMOS A TU PANTALLA NUEVA!
            AddEditWorkOrderScreen(
                navController = navController,
                orderId = id
            )
        }

        // --- FACTURAS ---
        composable("invoices") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                InvoiceListScreen(navController = navController)
            }
        }
    }
}