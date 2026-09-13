package com.ketchupzzz.analytical.models.quiz

data class CategoryWithQuiz(
    val category : String  = "",
    val quiz : List<Quiz> = emptyList()
)