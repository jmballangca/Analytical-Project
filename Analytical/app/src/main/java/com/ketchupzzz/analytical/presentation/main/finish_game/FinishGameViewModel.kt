package com.ketchupzzz.analytical.presentation.main.finish_game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.repository.submissions.SubmissionRepository
import com.ketchupzzz.analytical.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class FinishGameViewModel @Inject constructor(
     private val submissionRepository: SubmissionRepository
) : ViewModel() {
    var state by mutableStateOf(FinishGameState())

    fun events(e : FinishGameEvents) {
        when(e) {
            is FinishGameEvents.OnSubmit -> submit(e.submissions)
        }
    }

    private fun submit(submissions: Submissions) {
        viewModelScope.launch {
            submissionRepository.submitQuiz(submissions) {
                state = when(it) {
                    is UiState.Error -> state.copy(isLoading = false, errors = it.message)
                    is UiState.Loading -> state.copy(isLoading = true, errors = null)
                    is UiState.Success -> state.copy(isLoading = false, errors = null, gameSubmitted = it.data)
                }
            }
        }

    }
}