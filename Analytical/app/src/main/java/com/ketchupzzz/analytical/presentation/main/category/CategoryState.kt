package com.ketchupzzz.analytical.presentation.main.category

import com.ketchupzzz.analytical.models.quiz.Quiz


data class CategoryState(
    val isLoading : Boolean = false,
    val games : List<Quiz> = emptyList(),
    val errors : String ? = null,
)