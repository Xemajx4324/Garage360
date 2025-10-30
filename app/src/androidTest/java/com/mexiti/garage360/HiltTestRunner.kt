package com.mexiti.garage360

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        name: String?,
        context: Context?
    ): Application {
        // Aquí Hilt genera la aplicación de test
        return super.newApplication(cl, HiltTestApplication::class.java.name, context)
    }
}
