package com.ketchupzzz.analytical.presentation.auth.edit_profile



sealed interface EditProfileEvents {
    data class OnTextFieldChanged(val text  : String,val textFieldChange: TextFieldChange) : EditProfileEvents
    data object OnSave : EditProfileEvents
}