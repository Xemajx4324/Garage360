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
class ClientAdditionTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun test_addNewClient_isSuccessful() {
        val testName = "Juan Pérez"
        val testPhone = "5512345678"
        val testEmail = "juan.perez@test.com"

        // 1️⃣ Cargar MainScreen
        composeTestRule.setContent {
            Garage360Theme {
                MainScreen()
            }
        }

        // 2️⃣ Ir a la pantalla de añadir cliente
        composeTestRule.onNodeWithContentDescription("Agregar")
            .performClick()

        // 3️⃣ Esperar a que aparezca "Nuevo Cliente"
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodes(hasText("Nuevo Cliente")).fetchSemanticsNodes().isNotEmpty()
        }

        // 4️⃣ Llenar los campos
        composeTestRule.onNodeWithText("Nombre")
            .assertIsDisplayed()
            .performTextInput(testName)

        composeTestRule.onNodeWithText("Teléfono")
            .performTextInput(testPhone)

        composeTestRule.onNodeWithText("Email")
            .performTextInput(testEmail)

        // 5️⃣ Guardar cliente
        composeTestRule.onNodeWithText("Guardar Cliente")
            .performClick()

        // 6️⃣ Esperar a que aparezca el cliente en la lista
        composeTestRule.waitUntil(timeoutMillis = 10000) {
            composeTestRule.onAllNodes(hasText(testName)).fetchSemanticsNodes().isNotEmpty()
        }

        // 7️⃣ Verificaciones finales
        composeTestRule.onNodeWithText(testName).assertIsDisplayed()
        composeTestRule.onNodeWithText("Clientes").assertIsDisplayed()
    }
}
