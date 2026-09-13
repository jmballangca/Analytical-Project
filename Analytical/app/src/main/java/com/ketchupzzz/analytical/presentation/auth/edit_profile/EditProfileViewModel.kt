package com.ketchupzzz.analytical.presentation.auth.edit_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.analytical.repository.user.StudentRepository
import com.ketchupzzz.analytical.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class EditProfileViewModel @Inject constructor(
     private val studentRepository: StudentRepository
) : ViewModel() {
    var state by mutableStateOf(EditProfileState())

    init {
        viewModelScope.launch {
            studentRepository.getStudent().collect { data ->
                state = state.copy(
                    students = data,
                    firstName = data?.fname ?: "",
                    middleName =  data?.mname ?: "",
                    lastName =  data?.lname ?: "",
                )
            }
        }
    }

    fun events(e : EditProfileEvents) {
        when(e) {
            is EditProfileEvents.OnTextFieldChanged -> onTextChange(e.text,e.textFieldChange)
            EditProfileEvents.OnSave -> submit()
        }
    }

    private fun submit() {
        val firstname = state.firstName
        val middleName  = state.middleName
        val lastName = state.lastName
        viewModelScope.launch {
            studentRepository.editProfile(
                state.students?.id ?:"",
                firstName = firstname,
                middleName=middleName,
                lastName = lastName
            ) {
              state =  when(it) {
                    is UiState.Error -> state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    UiState.Loading ->  state.copy(
                        isLoading = true,
                        errors = null
                    )
                    is UiState.Success ->  state.copy(
                        isLoading = false,
                        errors = null,
                        success = it.data
                    )
                }

            }
        }
    }

    private fun onTextChange(text: String, textFieldChange: TextFieldChange) {
        state = when(textFieldChange) {
            TextFieldChange.FIRST -> state.copy(firstName = text)
            TextFieldChange.MIDDLE -> state.copy(middleName = text)
            TextFieldChange.LAST -> state.copy(lastName = text)
        }
    }
}