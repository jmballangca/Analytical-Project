package com.ketchupzzz.analytical.presentation.main

import android.media.Image
import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.presentation.navigation.AppRouter

data class BottomNavigationItems(
    val label : String,
    @DrawableRes val selectedIcon : Int,
    @DrawableRes val unselectedIcon : Int,
    val hasNews : Boolean,
    val badgeCount : Int? = null,
    val route : String
) {
    companion object {
        val ITEMS : List<BottomNavigationItems> = listOf(
            BottomNavigationItems(
                label = "Dashboard",
                selectedIcon = R.drawable.home_filled,
                unselectedIcon = R.drawable.home_outlined,
                hasNews = false,
                route = AppRouter.DashboardScreen.route
            ),
            BottomNavigationItems(
                label = "Leaderboard",
                selectedIcon = R.drawable.transactions_filled,
                unselectedIcon = R.drawable.transactions_outlined,
                hasNews = false,
                route = AppRouter.LeaderboardScreen.route
            ),
            BottomNavigationItems(
                label = "Profile",
                selectedIcon = R.drawable.user_filled,
                unselectedIcon = R.drawable.user_outlined,
                hasNews = false,
                route = AppRouter.ProfileScreen.route
            )
        )
    }
}