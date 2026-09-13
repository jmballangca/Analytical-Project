package com.ketchupzzz.analytical.presentation.main.profile

import android.net.Uri
import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.submissions.AverageScorePerCategory
import com.ketchupzzz.analytical.models.submissions.Submissions

data class ProfileState(
    val isLoading : Boolean = false,
    val students: Students ? = null,
    val isLoggedOut : String ? = null,
    val submissions : List<Submissions> = emptyList(),
    val errors : String ? = null,
    val quizGame : AverageScorePerCategory ? = null,
    val memoryGame : AverageScorePerCategory ? = null,
    val puzzleGame : AverageScorePerCategory ? = null,
    val mathGame : AverageScorePerCategory ? = null,
    val isProfileUploaded : String ? = null,
)