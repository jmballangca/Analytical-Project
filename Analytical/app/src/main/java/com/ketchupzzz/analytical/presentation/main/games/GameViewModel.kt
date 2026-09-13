package com.ketchupzzz.analytical.presentation.main.games

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.repository.questions.QuestionRepository
import com.ketchupzzz.analytical.repository.quiz.QuizRepository
import com.ketchupzzz.analytical.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class GameViewModel @Inject constructor(
     private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository
) : ViewModel() {
    var state by mutableStateOf(GameState())

    fun events(e : GameEvents) {
        when(e) {
            is GameEvents.OnGetGameByID -> getQuizWithLevels(e.id)
        }
    }



    private fun getQuizWithLevels(id: String) {
        viewModelScope.launch {
            quizRepository.getQuizByID(id) {
                state = when(it) {
                    is UiState.Error -> state.copy(errors = it.message, isLoading = false)
                    UiState.Loading -> state.copy(errors = null, isLoading = true)
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        quiz = it.data.quiz,
                        levels = it.data.levels.sortedBy { it.levelNumber },
                        submissions = it.data.submissions
                    )
                }
            }
        }
    }
}