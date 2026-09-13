package com.ketchupzzz.analytical.models.submissions

import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.Subject
import java.math.BigDecimal

data class GroupedSubmissions(
    val level : String,
    val highestScore : Double = 0.00,
    val list: List<Submissions>
)
fun List<Submissions>.groupSubmissionsByLevel(): List<GroupedSubmissions> {
    val mapped: Map<String, List<Submissions>> = this.groupBy { it.quizInfo?.levels?.id ?: "Unknown Level" }

    return mapped.map { (level, submissions) ->
        val highestScore = submissions.maxOfOrNull { it.performance.earning } ?: 0.0
        GroupedSubmissions(level, highestScore, submissions)
    }
}

fun List<GroupedSubmissions>.getPoints() : Double {
    var count = 0.0
    this.forEach {
        count += it.highestScore
    }
    return  count
}


data class AverageScorePerCategory(
    val category: Category, // Assuming category is a String or adjust accordingly
    val average: Double,
    val averageMaxScore : Double,
    val totalSubmissions: Int
)

fun List<Submissions>.groupSubmissionsByCategoryAndGetAverageScore(
    category: Category
): AverageScorePerCategory {
    val filteredSubmissions = this.filter { it.quizInfo?.category == category }
    val totalSubmissions = filteredSubmissions.size
    val totalScore = filteredSubmissions.sumOf { it.performance.earning }
    val averageTotalMaxScore = filteredSubmissions.sumOf {
        (it.quizInfo?.levels?.points?.toDouble() ?: 0.0) * (it.quizInfo?.levels?.questions?.toDouble() ?: 0.0)
    }
    val averageScore = if (totalSubmissions > 0) {
        totalScore / totalSubmissions
    } else {
        0.0
    }

    return AverageScorePerCategory(
        category = category,
        average = averageScore,
        averageMaxScore = averageTotalMaxScore,
        totalSubmissions = totalSubmissions
    )
}

data class AverageScorePerSubject(
    val subject: Subject, // Assuming category is a String or adjust accordingly
    val average: Double,
    val averageMaxScore : Double,
    val totalSubmissions: Int
)


fun List<Submissions>.getAverageScoreBySubject(
    type: Subject
): AverageScorePerSubject {
    val filteredSubmissions = this.filter { it.quizInfo?.type == type }
    val totalSubmissions = filteredSubmissions.size
    val totalScore = filteredSubmissions.sumOf { it.performance.earning }
    val averageTotalMaxScore = filteredSubmissions.sumOf {
        (it.quizInfo?.levels?.points?.toDouble() ?: 0.0) * (it.quizInfo?.levels?.questions?.toDouble() ?: 0.0)
    }
    val averageScore = if (totalSubmissions > 0) {
        totalScore / totalSubmissions
    } else {
        0.0
    }
    return AverageScorePerSubject(
        subject = type,
        average = averageScore,
        averageMaxScore = averageTotalMaxScore,
        totalSubmissions = totalSubmissions
    )
}