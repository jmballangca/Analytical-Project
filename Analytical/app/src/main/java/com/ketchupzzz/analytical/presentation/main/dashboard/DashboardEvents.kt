package com.ketchupzzz.analytical.presentation.main.dashboard

import com.ketchupzzz.analytical.models.Students


sealed interface DashboardEvents {
    data object OnGetAllQuizies : DashboardEvents
    data class OnSelect(val index : Int) : DashboardEvents

    data class OnSetStudent(val students: Students) : DashboardEvents
    data class OnGetStudentSubmission(val uid : String) : DashboardEvents
}