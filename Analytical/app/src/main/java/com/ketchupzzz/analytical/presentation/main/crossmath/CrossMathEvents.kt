package com.ketchupzzz.analytical.presentation.main.crossmath

import android.content.Context
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel

sealed interface CrossMathEvents  {
    data class OnSetStudent (val students: Students) : CrossMathEvents
    data class OnSetData(
        val quizAndLevel: QuizAndLevel,
        ) : CrossMathEvents
    data class OnGetCrossMathQuestion(
        val quizID : String,
        val levelID : String,
        val count : Int,
        val timer : Int
    ) : CrossMathEvents

    data class OnAnswerChange(
        val ans : String
    ) : CrossMathEvents
    data object OnDeleteAnswer : CrossMathEvents
    data object OnSubmit : CrossMathEvents
    data class OnNext(
        val index : Int
    ) : CrossMathEvents


    data class OnSave(
        val level : Levels,
        val quiz: Quiz,
        val context : Context
    ) : CrossMathEvents
}