package com.ketchupzzz.analytical.presentation.main.dashboard

import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.quiz.CategoryWithQuiz
import com.ketchupzzz.analytical.models.submissions.RecentlyPlayed
import com.ketchupzzz.analytical.models.submissions.Submissions


data class DashboardState(
    val isLoading : Boolean = false,
    val students: Students ? = null,
    val quizzes : List<CategoryWithQuiz> = listOf(),
    val errors : String ? = null,
    val selected : Int = 0,
    val recentlyPlayed: RecentlyPlayed ? = null,
    val submissions : List<Submissions> = emptyList(),
)