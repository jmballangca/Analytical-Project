package com.ketchupzzz.analytical.presentation.main.matching_card

import android.content.Context
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel


sealed interface MatchingCardEvents {

    data class OnSetStudent (val students: Students) : MatchingCardEvents
    data class OnSetData(
        val quizAndLevel: QuizAndLevel,
    ) : MatchingCardEvents


    data class CardClicked(val card: MatchingCard) : MatchingCardEvents
    data class MatchFound(val first : String,val second : String) : MatchingCardEvents
    data object RestartGame : MatchingCardEvents
    data object CheckForMatch : MatchingCardEvents
    data object ResetFlippedCards : MatchingCardEvents
    data class OnAddAnswerSheet(
        val question : Questions,
        val points : Int
    ) : MatchingCardEvents
    data class OnGetQuestions(
        val gameID : String,
        val levelID : String,
        val count : Int,

    ) : MatchingCardEvents

    data class OnNext(
        val index : Int
    ) : MatchingCardEvents



    data class OnSave(
        val context : Context
    ) : MatchingCardEvents
}