package com.ketchupzzz.analytical.presentation.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.presentation.navigation.MainNavGraph
import com.ketchupzzz.analytical.ui.custom.Avatar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier,mainNav: NavHostController) {
    val navHostController = rememberNavController()
    val navigationItems: List<BottomNavigationItems> = BottomNavigationItems.ITEMS

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Check if the current route requires a bottom bar
    if (navigationItems.any { it.route == currentRoute }) {
        val isDashBoard = currentRoute == AppRouter.DashboardScreen.route
        Scaffold(
            bottomBar = {
                BottomNavBar(navController = navHostController, items = navigationItems,navBackStackEntry = navBackStackEntry)
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = modifier
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
            ) {
                MainNavGraph(navHostController = navHostController, mainNavHostController = mainNav)
            }
        }
    } else {
        // No Scaffold, custom AppBar or other layout can be defined per route
        MainNavGraph(navHostController = navHostController,mainNav)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBarWithCustomTitle(
    modifier: Modifier = Modifier,
) {
    LargeTopAppBar(
        modifier = Modifier
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(color = Color.Transparent),
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        title = {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Juan Dela Cruz",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Avatar(image = "", size = 60.dp)
            }
        }
    )
}

