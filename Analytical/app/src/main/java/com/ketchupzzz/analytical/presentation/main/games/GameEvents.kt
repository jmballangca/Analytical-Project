package com.ketchupzzz.analytical.presentation.main.games

import com.ketchupzzz.analytical.models.Category

sealed interface GameEvents {
    data class OnGetGameByID(val id : String) : GameEvents
}