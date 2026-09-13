package com.ketchupzzz.analytical.presentation.main.leaderboard

import com.ketchupzzz.analytical.models.Students
import com.ketchupzzz.analytical.models.StudentsWithSubmissions
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.utils.mockLeaderboard

data class LeaderBoardState(
    val isLoading : Boolean = false,
    val errors : String ? = null,
    val students : List<Students> = emptyList(),
    val submissions: List<Submissions> = emptyList(),
    val leaderboard : List<StudentsWithSubmissions> = mockLeaderboard,
)