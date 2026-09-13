package com.ketchupzzz.analytical.presentation.auth.register

import com.ketchupzzz.analytical.models.SchoolLevel
import com.ketchupzzz.analytical.presentation.auth.login.LoginEvents
import com.ketchupzzz.analytical.utils.UiState


sealed interface RegistrationEvents  {
    data class OnFirstnameChanged(val value: String) : RegistrationEvents
    data class OnMiddlenameChanged(val value: String) : RegistrationEvents
    data class OnLastnameChanged(val value: String) : RegistrationEvents
    data class OnStudentIDChanged(val value: String) : RegistrationEvents
    data class OnSchoolLevelChanged(val value: SchoolLevel) : RegistrationEvents
    data class OnEmailChanged(val value: String) : RegistrationEvents
    data class OnPasswordChanged(val value: String) : RegistrationEvents
    data class OnConfirmPasswordChanged(val value: String) : RegistrationEvents
    data object OnTogglePasswordVisibility : RegistrationEvents
    data object OnToggleConfirmPasswordVisibility : RegistrationEvents
    data object OnSchoolLevelExpanded : RegistrationEvents
    data class OnSubmit(val result : (UiState<String>) -> Unit): RegistrationEvents
    data class CheckStudentID(val result : (UiState<Boolean>) -> Unit): RegistrationEvents
}