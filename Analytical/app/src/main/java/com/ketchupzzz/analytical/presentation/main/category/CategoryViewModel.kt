package com.ketchupzzz.analytical.presentation.main.category

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.analytical.repository.quiz.QuizRepository
import com.ketchupzzz.analytical.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class CategoryViewModel @Inject constructor(
     private val quizRepository: QuizRepository
) : ViewModel() {
    var state by mutableStateOf(CategoryState())

    fun events(e : CategoryEvents) {
        when(e){
            is CategoryEvents.GetGamesBySchoolLevelAndCategory ->getGames(e.category,e.schoolLevel)
        }
    }

    private fun getGames(category: String, schoolLevel: String) {
        viewModelScope.launch {
            quizRepository.getAllQuizBySchoolLevelAndCategory(schoolLevel,category) {
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success ->state.copy(
                        isLoading = false,
                        errors = null,
                        games = it.data,
                    )
                }
            }
        }
    }
}