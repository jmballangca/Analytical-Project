package com.ketchupzzz.analytical.repository.quiz

import androidx.compose.runtime.MutableState
import com.ketchupzzz.analytical.models.SchoolLevel
import com.ketchupzzz.analytical.models.quiz.CategoryWithQuiz
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.models.submissions.RecentlyPlayed
import com.ketchupzzz.analytical.presentation.main.games.data.QuizWithLevels
import com.ketchupzzz.analytical.utils.UiState





interface QuizRepository {
   suspend fun getAllGames(result : (UiState<List<Quiz>>) -> Unit)
   suspend fun getAllQuiz(result : (UiState<List<CategoryWithQuiz>>) -> Unit)
   suspend fun getQuizByID(id : String,result: (UiState<QuizWithLevels>) -> Unit)
   suspend fun getAllQuizBySchoolLevel(
      schoolLevel: String,
      result: (UiState<List<Quiz>>) -> Unit
   )
   suspend fun getAllQuizBySchoolLevelAndCategory(
      schoolLevel: String,
      category : String,
      result: (UiState<List<Quiz>>) -> Unit
   )


   suspend fun getRecentlyPlayedGame(userID : String,result: (UiState<RecentlyPlayed?>) -> Unit)
}