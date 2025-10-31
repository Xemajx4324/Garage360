package com.mexiti.garage360.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.hasText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mexiti.garage360.ui.theme.Garage360Theme
import com.mexiti.garage360.ui.views.MainScreen
import com.mexiti.garage360.HiltTestActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class AddWorkOrderTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun test_addNewWorkOrder_isSuccessful() {
        val vehicleMake = "Honda"
        val vehicleModel = "Civic"
        val vehicleYear = "2023"
        val licensePlate = "XYZ-789"
        val description = "Brake inspection"

        // 1. Cargar MainScreen
        composeTestRule.setContent {
            Garage360Theme {
                MainScreen()
            }
        }

        // 2. Ir a la pantalla de añadir orden de trabajo
        composeTestRule.onNodeWithContentDescription("Agregar")
            .performClick()

        // 3. Esperar a que aparezca "Nueva Orden"
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodes(hasText("Nueva Orden")).fetchSemanticsNodes().isNotEmpty()
        }

        // 4. Llenar los campos
        composeTestRule.onNodeWithText("Marca del Vehículo")
            .assertIsDisplayed()
            .performTextInput(vehicleMake)

        composeTestRule.onNodeWithText("Modelo")
            .performTextInput(vehicleModel)

        composeTestRule.onNodeWithText("Año")
            .performTextInput(vehicleYear)

        composeTestRule.onNodeWithText("Placas")
            .performTextInput(licensePlate)

        composeTestRule.onNodeWithText("Servicio a Realizar")
            .performTextInput(description)

        // 5. Guardar orden de trabajo
        composeTestRule.onNodeWithText("Guardar Orden")
            .performClick()

        // 6. Esperar a que aparezca la orden de trabajo en la lista
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodes(hasText("$vehicleMake $vehicleModel ($vehicleYear)")).fetchSemanticsNodes().isNotEmpty()
        }

        // 7. Verificaciones finales
        composeTestRule.onNodeWithText("$vehicleMake $vehicleModel ($vehicleYear)").assertIsDisplayed()
    }
}