package com.ketchupzzz.analytical.presentation.main.games

import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.models.submissions.Submissions


data class GameState(
    val isLoading : Boolean = false,
    val quiz : Quiz ? = null,
    val levels : List<Levels> = emptyList(),
    val submissions : List<Submissions> = emptyList(),
    val errors : String ? = null,
    val questions : List<Questions> = emptyList()
)