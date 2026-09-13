package com.ketchupzzz.analytical.presentation.main.profile

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.submissions.groupSubmissionsByCategoryAndGetAverageScore
import com.ketchupzzz.analytical.presentation.navigation.AppRouter
import com.ketchupzzz.analytical.repository.submissions.SubmissionRepository
import com.ketchupzzz.analytical.repository.user.StudentRepository
import com.ketchupzzz.analytical.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class ProfileViewModel @Inject constructor(
     private val studentRepository: StudentRepository,
    private val submissionRepository: SubmissionRepository
) : ViewModel() {
    var state by mutableStateOf(ProfileState())
    init {
        viewModelScope.launch {
            studentRepository.getStudentProfile {  data ->
                state = when(data) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = data.message
                    )

                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )

                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        students =data.data
                    )
                }
            }
        }
    }
    fun events(e : ProfileEvents) {
        when(e) {
            is ProfileEvents.OnLoggedOut -> logout(e.navHostController)
            is ProfileEvents.OnGetSubmissions -> getAllSubmissions(e.sid)
            is ProfileEvents.OnUploadProfile -> upload(e.uri)
        }
    }

    private fun upload(uri: Uri) {
        if (state.students == null ) {
            return
        }
        viewModelScope.launch {
            studentRepository.updateProfile(
                state.students?.id ?: "",
                uri
            ){
                state = when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading -> state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success -> state.copy(
                        isLoading = false,
                        errors = null,
                        isProfileUploaded = it.data
                    )
                }
            }
        }
    }

    private fun getAllSubmissions(studentID: String) {
        viewModelScope.launch {
            submissionRepository.getAllSubmissions(studentID) {
                if (it is UiState.Success) {
                    state = state.copy(
                        submissions = it.data,
                        mathGame = it.data.groupSubmissionsByCategoryAndGetAverageScore(Category.MATH_GAME),
                        memoryGame = it.data.groupSubmissionsByCategoryAndGetAverageScore(Category.MEMORY_GAME),
                        quizGame = it.data.groupSubmissionsByCategoryAndGetAverageScore(Category.QUIZ_GAME),
                        puzzleGame = it.data.groupSubmissionsByCategoryAndGetAverageScore(Category.PUZZLE_GAME)
                    )
                }
            }
        }
    }

    private fun logout(navHostController: NavHostController) {
        viewModelScope.launch {
            studentRepository.logout {
                 when(it) {
                    is UiState.Error ->state = state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading ->
                        state =  state.copy(isLoading = true, errors = null)
                    is UiState.Success -> {
                        state = state.copy(isLoading = false, errors = null, isLoggedOut = it.data)
                        navHostController.navigate(AppRouter.AuthRoutes.route)
                    }
                }
            }
        }

    }
}