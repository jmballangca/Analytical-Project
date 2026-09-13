package com.ketchupzzz.analytical.presentation.main.leaderboard

import com.ketchupzzz.analytical.presentation.navigation.AppRouter


sealed interface LeaderboardEvents {
    data object OnGetStudents : LeaderboardEvents
    data object OnGetSubmissions : LeaderboardEvents

}