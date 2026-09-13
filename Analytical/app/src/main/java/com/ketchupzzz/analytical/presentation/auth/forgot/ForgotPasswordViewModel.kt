package com.ketchupzzz.analytical.presentation.auth.forgot

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.PendingIntentCompat.send
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.analytical.repository.user.StudentRepository
import com.ketchupzzz.analytical.utils.hasSpaces
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel

class ForgotPasswordViewModel @Inject constructor(
     private val studentRepository: StudentRepository
) : ViewModel() {

    var state by mutableStateOf(ForgotPasswordState())
    fun onEvents(events: ForgotPasswordEvents) {
        when(events) {
            is ForgotPasswordEvents.OnEmailChanged -> handleEmailChanged(events.email)
            is ForgotPasswordEvents.OnSendEmail -> send(events.email)
        }
    }

    private fun send(email: String) {
        viewModelScope.launch {
            state = state.copy(
                isLoading  = true
            )
            studentRepository.forgotPassword(email).onSuccess {
                state = state.copy(
                    isLoading = false,
                    isComplete = it.toString()
                )
            }.onFailure {
                state = state.copy(
                    isLoading = false,
                    errors = it.message.toString()
                )
            }
        }
    }

    private fun handleEmailChanged(email: String) {
        val hasError = !Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.hasSpaces()
        val errorMessage = if (hasError) "Invalid email" else null
        state = state.copy(email = state.email.copy(value = email, isError = hasError, errorMessage = errorMessage))
    }
}