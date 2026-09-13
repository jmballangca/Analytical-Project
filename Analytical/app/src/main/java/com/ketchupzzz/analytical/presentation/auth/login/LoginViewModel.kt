package com.ketchupzzz.analytical.presentation.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ketchupzzz.analytical.repository.user.StudentRepository
import com.ketchupzzz.analytical.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel

class LoginViewModel @Inject constructor(
    private val studentRepository: StudentRepository
) : ViewModel() {
    var state by mutableStateOf(LoginState())

    init {
        studentRepository.currentUser()?.let {
            it.email?.let {
                studentRepository.getUserByEmail(it) {
                    when(it) {
                        is UiState.Error -> {
                            state = state.copy(
                                isLoading = false,
                                error = it.message
                            )
                        }
                        UiState.Loading -> {
                            state = state.copy(
                                isLoading = true
                            )
                        }
                        is UiState.Success -> {
                            studentRepository.setStudent(it.data)
                            state = state.copy(
                                isLoading = false,
                                isLoggedIn = true
                            )
                        }
                    }
                }
            }

        }
    }

    fun onEvent(events: LoginEvents) {
        when(events) {
            is LoginEvents.OnLogin -> {
                val id = state.studentID
                val password = state.password
                if (id.value.isEmpty() || password.value.isEmpty()) {
                    state = state.copy(
                        studentID = id.copy(
                            isError = true,
                            errorMessage = "Student ID cannot be empty"
                        ),
                        password = password.copy(
                            isError = true,
                            errorMessage = "Password cannot be empty"
                        )
                    )
                    return
                }
                studentRepository.login(id.value, password.value) {
                    when (it) {
                        is UiState.Error -> {
                            state = state.copy(
                                isLoading = false,
                                error = it.message
                            )
                        }
                        is UiState.Loading -> {
                            state = state.copy(
                                isLoading = true
                            )
                        }
                        is UiState.Success -> {
                            studentRepository.setStudent(it.data)
                            state = state.copy(
                                isLoading = false,
                                isLoggedIn = true
                            )

                        }
                    }
                }

            }
            is LoginEvents.OnPasswordChanged -> {
                val password = events.password
                val currentPassword = state.password.copy(
                    value = password,
                    isError = false,
                )
                state = state.copy(
                    password =  currentPassword
                )
            }
            is LoginEvents.OnStudentIDChanged -> {
                val studentID = events.studentID
                val currentStudentID = state.studentID.copy(
                    value = studentID,
                )
                state = state.copy(
                    studentID =  currentStudentID
                )
            }
            is LoginEvents.OnTogglePasswordVisibility -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

        }
    }
}