package com.ketchupzzz.analytical.presentation.main.matching_card

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.submissions.AnswerSheet
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel
import com.ketchupzzz.analytical.utils.generateRandomString
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject



data class MatchingCard(
    val id : String = generateRandomString(4),
    val isFront : Boolean,
    val choice : String,
    val globalIndex : Int

)

data class MatchingCardState(

    val quizAndLevel: QuizAndLevel? = null,
    val isLoading : Boolean = false,
    val selectedIndex : Int = 0,
    val errors : String ? = null,
    val answerSheet : List<AnswerSheet> = emptyList(),
    val students : Students? = null,
    val cards: List<MatchingCard> = emptyList(),
    val isCardFlipped: Boolean = false,
    val firstFlippedCard: MatchingCard? = null,
    val secondFlippedCard: MatchingCard? = null,
  val isGameOver: Boolean = false,
    val matches : List<String> = emptyList(),
    val questions : List<Questions> = emptyList(),
    val isGameEnd : Boolean = false
)



