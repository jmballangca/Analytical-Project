package com.ketchupzzz.analytical.presentation.main.search

import com.ketchupzzz.analytical.models.quiz.CategoryWithQuiz
import com.ketchupzzz.analytical.models.quiz.Quiz


data class SearchState(
    val isLoading : Boolean = false,
    val games : List<Quiz> = emptyList(),
    val filteredGames : List<Quiz> = emptyList(),
    val searchText : String = "",
    val errors : String ? = null
)