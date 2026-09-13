package com.ketchupzzz.analytical.presentation.main.difficulty_screen

import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.presentation.main.games.data.LevelsWithSubmissions


data class DifficultyState(
    val isLoading : Boolean = false,
    val quiz : Quiz? = null,
    val levels : List<Levels> = emptyList(),
    val submissions : List<Submissions> = emptyList(),
    val errors : String ? = null,
    val levelsWithSubmissions: List<LevelsWithSubmissions> = emptyList()
)

