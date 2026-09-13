package com.ketchupzzz.analytical.presentation.main.leaderboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.analytical.models.displayLeaderBoard
import com.ketchupzzz.analytical.repository.submissions.SubmissionRepository
import com.ketchupzzz.analytical.repository.user.StudentRepository
import com.ketchupzzz.analytical.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaderBoardViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val submissionRepository: SubmissionRepository
) : ViewModel() {
    var state by mutableStateOf(LeaderBoardState())


    init {
        events(LeaderboardEvents.OnGetStudents)

    }
    fun events(e: LeaderboardEvents) {
        when (e) {
            LeaderboardEvents.OnGetStudents -> getStudents()
            LeaderboardEvents.OnGetSubmissions -> getSubmissions()
        }
    }

    private fun getSubmissions() {
        viewModelScope.launch {
            submissionRepository.getAllSubmissions { result ->
                when(result) {
                    is UiState.Error -> state = state.copy(
                        isLoading = false,
                        errors = result.message
                    )
                    UiState.Loading -> state  = state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state = state.copy(
                        isLoading = false,
                        errors = null,
                        submissions = result.data,
                        leaderboard = state.students.displayLeaderBoard(result.data)
                    )
                }

            }
        }
    }

    private fun getStudents() {
        viewModelScope.launch {

            studentRepository.getStudents { result ->
                state = when(result) {
                    is UiState.Error ->state.copy(
                        isLoading = false,
                        errors = result.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> {

                        events(LeaderboardEvents.OnGetSubmissions)
                        state.copy(
                            isLoading = false,
                            errors = null,
                            students = result.data
                        )
                    }
                }

            }
        }
    }
}
