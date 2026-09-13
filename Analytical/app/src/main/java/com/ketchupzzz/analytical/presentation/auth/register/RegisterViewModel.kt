package com.ketchupzzz.analytical.presentation.auth.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ketchupzzz.analytical.models.SchoolLevel
import com.ketchupzzz.analytical.repository.user.StudentRepository
import com.ketchupzzz.analytical.utils.UiState
import com.ketchupzzz.analytical.utils.hasNumbers
import com.ketchupzzz.analytical.utils.hasSpaces
import com.ketchupzzz.analytical.utils.hasSpecialCharacters
import com.ketchupzzz.analytical.utils.isLessThanSix
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

import android.util.Patterns

@HiltViewModel

class RegisterViewModel @Inject constructor(
     private val studentRepository : StudentRepository
) : ViewModel() {
    var state by mutableStateOf(RegisterState())

    fun onEvents(events : RegistrationEvents) {
        when(events) {
            is RegistrationEvents.OnFirstnameChanged -> {
                val name = events.value
                val hasError = name.hasNumbers()
                val errorMessage = if (hasError)  {
                    "Invalid name"
                } else {
                    null
                }
                state =  state.copy(
                    firstName =state.firstName.copy(
                        value = name,
                        isError = hasError,
                        errorMessage = errorMessage
                    ),
                )
            }
            is RegistrationEvents.OnMiddlenameChanged -> {
                val name = events.value
                val hasError = name.hasNumbers()
                val errorMessage = if (hasError)  {
                    "Invalid name"
                } else {
                    null
                }
                state =  state.copy(
                    middleName =state.middleName.copy(
                        value = name,
                        isError = hasError,
                        errorMessage = errorMessage
                    ),
                )
            }
            is RegistrationEvents.OnLastnameChanged -> {
                val name = events.value
                val hasError = name.hasNumbers()
                val errorMessage = if (hasError)  {
                    "Invalid name"
                } else {
                    null
                }
                state =  state.copy(
                    lastName =state.lastName.copy(
                        value = name,
                        isError = hasError,
                        errorMessage = errorMessage
                    ),
                )
            }

            is RegistrationEvents.OnSchoolLevelChanged -> {
                state = state.copy(
                    schoolLevel = events.value,
                )
            }
            is RegistrationEvents.OnStudentIDChanged -> {
                state =  state.copy(
                    studentID = events.value
                )
            }
            is RegistrationEvents.OnEmailChanged -> {
                val email = events.value
                val hasError =  !Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.hasSpaces()
                val errorMessage = if (hasError)  {
                    "Invalid email"
                } else {
                    null
                }
                val newEmail = state.email.copy(
                    value = email,
                    isError = hasError,
                    errorMessage = errorMessage
                )
                state = state.copy(
                    email = newEmail,
                )
            }
            is RegistrationEvents.OnPasswordChanged -> {
                val password = events.value
                val hasError = password.isLessThanSix() || password.hasSpaces()
                val errorMessage = if (password.isLessThanSix()) {
                    "Password must be at least 6 characters"
                } else if (password.hasSpaces()) {
                    "Password cannot contain spaces"
                } else {
                    null
                }
                val currentPassword = state.password.copy(
                    value = password,
                    isError = hasError,
                    errorMessage = errorMessage
                )
                state = state.copy(
                    password =  currentPassword
                )
            }
            is RegistrationEvents.OnConfirmPasswordChanged -> {
                val password = events.value
                val hasError = password.isLessThanSix() || password.hasSpaces() || state.password.value != password
                val errorMessage = if (password.isLessThanSix()) {
                    "Password must be at least 6 characters"
                } else if (password.hasSpaces()) {
                    "Password cannot contain spaces"
                } else if (state.password.value != password) {
                    "Passwords do not match"
                } else {
                    null
                }
                val currentPassword = state.password.copy(
                    value = password,
                    isError = hasError,
                    errorMessage = errorMessage
                )
                state = state.copy(
                    confirmPassword =  currentPassword
                )
            }
            is RegistrationEvents.OnSubmit -> {
                studentRepository.register(
                    state.studentID,
                    state.firstName.value,
                    state.middleName.value,
                    state.lastName.value,
                    state.schoolLevel,
                    state.email.value,
                    state.password.value,
                    events.result,
                )
            }

            RegistrationEvents.OnSchoolLevelExpanded -> {
                state = state.copy(
                    isSchoolLevelExpanded = !state.isSchoolLevelExpanded
                )
            }

            RegistrationEvents.OnToggleConfirmPasswordVisibility -> {
                state = state.copy(
                    isConfirmPasswordVisible = !state.isPasswordVisible
                )

            }
            RegistrationEvents.OnTogglePasswordVisibility -> {
                state = state.copy(
                    isPasswordVisible = !state.isPasswordVisible
                )
            }

            is RegistrationEvents.CheckStudentID -> {
                if (state.studentID.isNotEmpty()) {
                    studentRepository.getStudentByID(state.studentID,events.result)
                }

            }
        }
    }

}