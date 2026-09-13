package com.ketchupzzz.analytical.presentation.auth.login

import android.util.Log

sealed interface LoginEvents {
    data class OnStudentIDChanged(val studentID: String) : LoginEvents
    data class OnPasswordChanged(val password: String) : LoginEvents
    data object OnTogglePasswordVisibility : LoginEvents
    data object OnLogin : LoginEvents
}