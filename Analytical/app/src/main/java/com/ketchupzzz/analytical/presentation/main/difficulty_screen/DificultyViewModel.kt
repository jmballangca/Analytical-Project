package com.ketchupzzz.analytical.presentation.main.difficulty_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.analytical.repository.quiz.QuizRepository
import com.ketchupzzz.analytical.utils.UiState
import com.ketchupzzz.analytical.utils.toLevelWithSubmissions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DifficultyViewModel @Inject constructor(
    private val quizRepository: QuizRepository
) : ViewModel() {
    var state by mutableStateOf(DifficultyState())
    fun events(e : DifficultyEvents) {
        when(e) {
            is DifficultyEvents.OnGetGameByID -> getQuizWithLevels(e.id)
        }
    }


    private fun getQuizWithLevels(id: String) {
        viewModelScope.launch {
            quizRepository.getQuizByID(id) {
                state = when(it) {
                    is UiState.Error -> state.copy(errors = it.message, isLoading = false)
                    UiState.Loading -> state.copy(errors = null, isLoading = true)
                    is UiState.Success -> {
                        val submissions = it.data.submissions
                        val levels = it.data.levels.sortedBy { it.levelNumber }
                        val levelsWithSubmissions = levels.toLevelWithSubmissions(submissions)
                        state.copy(
                            isLoading = false,
                            errors = null,
                            quiz = it.data.quiz,
                            levels = levels,
                            submissions = submissions,
                            levelsWithSubmissions = levelsWithSubmissions
                        )
                    }
                }
            }
        }
    }
}