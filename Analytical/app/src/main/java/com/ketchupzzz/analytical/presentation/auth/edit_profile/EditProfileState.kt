package com.ketchupzzz.analytical.presentation.auth.edit_profile

import com.ketchupzzz.analytical.models.Students


data class EditProfileState(
    val isLoading : Boolean = false,
    val students: Students? = null,
    val firstName : String = "",
    val middleName : String = "",
    val lastName : String = "",
    val errors : String ? = null,
    val success : String ? = null,
)

enum class TextFieldChange {
    FIRST,
    MIDDLE,
    LAST
}