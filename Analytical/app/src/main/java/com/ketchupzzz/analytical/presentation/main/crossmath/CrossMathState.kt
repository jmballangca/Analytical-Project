package com.ketchupzzz.analytical.presentation.main.crossmath

import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.submissions.AnswerSheet
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel
import com.ketchupzzz.analytical.presentation.main.games.data.QuizWithLevels


data class CrossMathState(
    val students : Students ? = null,
    val quizAndLevel: QuizAndLevel? = null,
    val isLoading : Boolean = false,
    val errors : String ? = null,
    val questions : List<Questions> = emptyList(),
    val selectedIndex : Int = 0,
    val answer : String = "",
    val wrongAnswer : String ? = null,
    val isGameEnd : Boolean = false,
    val answerSheet : List<AnswerSheet> = emptyList()
)