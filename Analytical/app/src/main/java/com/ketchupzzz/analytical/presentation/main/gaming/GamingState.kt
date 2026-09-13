package com.ketchupzzz.analytical.presentation.main.gaming

import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.submissions.AnswerSheet

data class GamingState(
    val isLoading : Boolean = false,
    val errors : String ? = null,
    val students : Students ? = null,
    val questions : List<Questions> = emptyList(),
    val answerSheet : List<AnswerSheet> = emptyList(),
    val timer : Int = -1,
    val questionCount : Int = 0,
    val answerIndex : List<Int> = emptyList(),
    val answers : String  = "",
    val isFinish : Boolean = false,
)
