package com.mexiti.garage360.ui.views


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.mexiti.garage360.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Estado para el Drawer
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope() // Para abrir/cerrar el drawer

    val navigationItems = listOf(
        // El primer ítem de la barra inferior ahora es el Drawer
        NavigationItem("Menu", Icons.Default.Menu, "menu"),
        NavigationItem("Vehículos", Icons.Default.DirectionsCar, "workOrderList"),
        NavigationItem("Clientes", Icons.Default.People, "clientList"),
        NavigationItem("Facturas", Icons.Default.RequestQuote, "facturas")
    )

    val mainScreenRoutes = listOf("workOrderList", "clientList", "facturas")
    val showMainUI = currentRoute in mainScreenRoutes

    val currentTitle = when (currentRoute) {
        "workOrderList" -> "Garage 360"
        "clientList" -> "Clientes"
        "facturas" -> "Facturación"
        else -> ""
    }


        ModalNavigationDrawer(

            drawerState = drawerState,
            drawerContent = {
                // Contenido del menú lateral
                ModalDrawerSheet(
                    drawerContainerColor = Color.Black             )
                {

                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_image),
                        contentDescription = "Fondo principal del taller",
                        modifier = Modifier.matchParentSize().alpha(1f)
                    )

                        Column(modifier = Modifier.fillMaxSize()) {

                            Text(
                                "Garage 360",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 28.sp
                                )
                            )
                            Divider()
                            NavigationDrawerItem(
                                label = { Text(text = "Órdenes de Trabajo") },
                                selected = currentRoute == "workOrderList",
                                onClick = {
                                    navController.navigate("workOrderList") {
                                        popUpTo(navController.graph.startDestinationId); launchSingleTop =
                                        true
                                    }
                                    scope.launch { drawerState.close() } // Cierra el drawer
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.DirectionsCar,
                                        contentDescription = "Órdenes"
                                    )
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text(text = "Clientes") },
                                selected = currentRoute == "clientList",
                                onClick = {
                                    navController.navigate("clientList") {
                                        popUpTo(navController.graph.startDestinationId); launchSingleTop =
                                        true
                                    }
                                    scope.launch { drawerState.close() } // Cierra el drawer
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.People,
                                        contentDescription = "Clientes"
                                    )
                                }
                            )
                            NavigationDrawerItem(
                                label = { Text(text = "Facturación") },
                                selected = currentRoute == "facturas",
                                onClick = {
                                    navController.navigate("facturas") {
                                        popUpTo(navController.graph.startDestinationId); launchSingleTop =
                                        true
                                    }
                                    scope.launch { drawerState.close() } // Cierra el drawer
                                },
                                icon = {
                                    Icon(
                                        Icons.Default.RequestQuote,
                                        contentDescription = "Facturas")
                                }
                            )
                        }
                    }
                }

            }
        ) {


            Scaffold(

                topBar = {
                    if (showMainUI) {
                        CenterAlignedTopAppBar(
                            title = { Text(currentTitle, fontWeight = FontWeight.Bold) },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() } // Abre el drawer
                                }) {
                                    Icon(Icons.Filled.Menu, contentDescription = "Abrir Menú")
                                }
                            },
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary // Color del icono de menú
                            )
                        )
                    }
                },
                floatingActionButton = {
                    if (showMainUI) {
                        FloatingActionButton(
                            onClick = {
                                if (currentRoute == "clientList") {
                                    navController.navigate("addEditClient/0")
                                } else { // Asume que es workOrderList o facturas (ajustar si es necesario)
                                    navController.navigate("addEditWorkOrder/0")
                                }
                            },
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Agregar")
                        }
                    }
                },
                floatingActionButtonPosition = FabPosition.Center,
                bottomBar = {
                    if (showMainUI) {
                        BottomAppBar(
                            actions = {
                                // Iteramos sobre los ítems para la barra inferior
                                // Podrías tener una lista separada si quieres ítems distintos en el drawer y la barra
                                navigationItems.forEach { item ->
                                    // El ítem "Menu" se ignora en la barra inferior
                                    if (item.route != "menu") {
                                        NavigationBarItem(
                                            selected = currentRoute == item.route,
                                            onClick = {
                                                navController.navigate(item.route) {
                                                    popUpTo(navController.graph.startDestinationId)
                                                    launchSingleTop = true
                                                }
                                            },
                                            icon = {
                                                Icon(
                                                    item.icon,
                                                    contentDescription = item.label
                                                )
                                            },
                                            label = { Text(item.label) }
                                        )
                                    }
                                }
                            },
                            floatingActionButton = {} // Deja el espacio para el FAB
                        )
                    }
                }
            ) { innerPadding ->
                // El NavHost sigue igual, pero recibe el padding del Scaffold
                AppNavHost(navController = navController, padding = innerPadding)
            }
        }
    }

@Composable
fun AppNavHost(navController: NavHostController, padding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = "workOrderList",
        modifier = Modifier.padding(padding)
    ) {
        composable("workOrderList") {
            WorkOrderListScreen(navController = navController) // No necesita ViewModel explícito
        }
        composable("clientList") {
            ClientListScreen(navController = navController) // No necesita ViewModel explícito
        }
        composable("facturas") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Pantalla de Facturas")
            }
        }

        composable(
            "addEditWorkOrder/{orderId}",
            arguments = listOf(navArgument("orderId") { type = NavType.LongType })
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getLong("orderId") ?: 0L
            AddEditWorkOrderScreen(navController, orderId, viewModel = hiltViewModel())
        }

        composable(
            "addEditClient/{clientId}",
            arguments = listOf(navArgument("clientId") { type = NavType.LongType })
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getLong("clientId") ?: 0L
            AddEditClientScreen(navController, clientId, viewModel = hiltViewModel())
        }
    }
}