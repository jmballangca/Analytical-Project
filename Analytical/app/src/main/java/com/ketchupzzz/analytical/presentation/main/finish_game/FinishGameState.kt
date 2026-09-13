package com.ketchupzzz.analytical.presentation.main.finish_game

data class FinishGameState(
    val isLoading : Boolean = false,
    val gameSubmitted : String ? = null,
    val errors : String ? = null
)
