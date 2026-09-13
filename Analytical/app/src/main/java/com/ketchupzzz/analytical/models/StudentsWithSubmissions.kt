package com.ketchupzzz.analytical.models

import android.annotation.SuppressLint
import com.ketchupzzz.analytical.models.submissions.GroupedSubmissions
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.models.submissions.groupSubmissionsByLevel
import com.ketchupzzz.analytical.presentation.main.games.components.displayLeaderBoard




data class StudentsWithSubmissions(
    val students: Students? = null,
    val submissions: List<GroupedSubmissions> = emptyList(),
    val totalPoints: Double = 0.00,
)

@SuppressLint("DefaultLocale")
fun List<Students>.displayLeaderBoard(submissions: List<Submissions>): List<StudentsWithSubmissions> {
    val studentsWithSubmissions = mutableListOf<StudentsWithSubmissions>()

    this.forEach { student ->
        val studentSubmissions = submissions.filter { it.studentID == student.id }.groupSubmissionsByLevel()
        var totalPoints = 0.00

        studentSubmissions.forEach { submission ->
            totalPoints += submission.highestScore
        }

        // Format totalPoints to 2 decimal places
        val formattedTotalPoints = String.format("%.2f", totalPoints).toDouble()

        studentsWithSubmissions.add(
            StudentsWithSubmissions(
                students = student,
                submissions = studentSubmissions,
                totalPoints = formattedTotalPoints
            )
        )
    }

    return studentsWithSubmissions.sortedByDescending {
        it.totalPoints
    }
}
