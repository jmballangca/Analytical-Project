package com.ketchupzzz.analytical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ketchupzzz.analytical.presentation.main.MainScreen
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.presentation.navigation.authNavGraph

import com.ketchupzzz.analytical.ui.theme.AnalyticalTheme

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnalyticalTheme {
                val windowSize = calculateWindowSizeClass(activity = this)
                AnalyticalApp(windowSizeClass = windowSize)
            }

        }
    }
}

@Composable
fun AnalyticalApp(windowSizeClass: WindowSizeClass) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppRouter.AuthRoutes.route) {
        authNavGraph(navController)
        composable(route = AppRouter.MainRoutes.route) {
            MainScreen(mainNav = navController)
        }
    }
}