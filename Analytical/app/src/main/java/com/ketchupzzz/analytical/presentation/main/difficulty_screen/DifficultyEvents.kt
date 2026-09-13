package com.ketchupzzz.analytical.presentation.main.difficulty_screen

import com.ketchupzzz.analytical.presentation.main.games.GameEvents


sealed interface DifficultyEvents {
    data class OnGetGameByID(val id : String) : DifficultyEvents
}