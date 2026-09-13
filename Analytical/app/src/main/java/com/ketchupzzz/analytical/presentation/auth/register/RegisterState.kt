package com.ketchupzzz.analytical.presentation.auth.register

import com.ketchupzzz.analytical.models.SchoolLevel
import com.ketchupzzz.analytical.utils.Email
import com.ketchupzzz.analytical.utils.Fullname
import com.ketchupzzz.analytical.utils.Password
import com.ketchupzzz.analytical.utils.UiState


data class RegisterState(
    val firstName: Fullname = Fullname(),
    val middleName: Fullname = Fullname(),
    val lastName: Fullname = Fullname(),
    val studentID : String = "",
    val schoolLevel: SchoolLevel = SchoolLevel.GRADE_7,
    val email: Email = Email(),
    val password: Password = Password(),
    val confirmPassword: Password = Password(),
    val isConfirmPasswordVisible : Boolean=false,
    val isPasswordVisible : Boolean=false,
    val isSchoolLevelExpanded : Boolean=false,
)