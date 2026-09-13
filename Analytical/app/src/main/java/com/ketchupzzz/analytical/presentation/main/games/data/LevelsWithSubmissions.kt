package com.ketchupzzz.analytical.presentation.main.games.data

import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.submissions.Submissions

data class LevelsWithSubmissions(
    val levels: Levels ,
    val submissions: List<Submissions> = emptyList()
)
