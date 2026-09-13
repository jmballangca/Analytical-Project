package com.ketchupzzz.analytical.presentation.auth.forgot

import com.ketchupzzz.analytical.utils.Email


data class ForgotPasswordState(
    val email : Email = Email(),
    val isLoading : Boolean = false,
    val errors : String? = null,
    val isComplete : String?  = null,
)