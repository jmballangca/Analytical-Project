package com.ketchupzzz.analytical.presentation.main.gaming

import android.content.Context
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz


sealed interface GamingEvents {
    data class OnGetQuestions(
        val gameID : String,
        val levelID : String,
        val count : Int,
        val timer : Int
    ) : GamingEvents
    data class OnExitGame(val navHostController: NavHostController) : GamingEvents
    data class OnSelectAnswer(val questions: Questions,val answer : String,val points : Int) : GamingEvents
    data class OnSubmit(
        val level : Levels,
        val quiz: Quiz,
        val context : Context
    ) : GamingEvents
    data class OnUpdateTimer(val time : Int) : GamingEvents
    data class OnAnswerChanged(val text : String,val index : Int) : GamingEvents
    data object OnReset : GamingEvents
}