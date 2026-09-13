package com.ketchupzzz.analytical.presentation.auth.login

import com.ketchupzzz.analytical.utils.Password
import com.ketchupzzz.analytical.utils.StudentID




data class LoginState(
    val studentID : StudentID = StudentID(),
    val password : Password = Password(),
    val isPasswordVisible : Boolean = false,
    val isLoading : Boolean = false,
    val error : String? = null,
    val isLoggedIn : Boolean = false,
)



