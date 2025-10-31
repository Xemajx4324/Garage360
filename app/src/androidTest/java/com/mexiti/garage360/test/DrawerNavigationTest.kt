package com.mexiti.garage360.test

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
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
class DrawerNavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun test_drawerNavigation_toClientsScreen() {
        // 1. Cargar MainScreen
        composeTestRule.setContent {
            Garage360Theme {
                MainScreen()
            }
        }

        // 2. Abrir el menú lateral
        composeTestRule.onNodeWithContentDescription("Abrir Menú")
            .performClick()

        // 3. Navegar a la pantalla de Clientes
        composeTestRule.onNodeWithText("Clientes")
            .performClick()

        // 4. Verificar que la pantalla de Clientes se muestra
        composeTestRule.onNodeWithText("Clientes").assertIsDisplayed()
    }
}