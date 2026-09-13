package com.ketchupzzz.analytical.models.submissions

import android.os.Parcelable
import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.Subject
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.quiz.Levels
import kotlinx.parcelize.Parcelize
import java.util.Date


@Parcelize
data class Submissions(
    var id : String ? = null,
    val studentID: String ? = null,
    val quizInfo: QuizInfo ? = null,
    val answerSheet : List<AnswerSheet> = mutableListOf(),
    val performance: Performance = Performance(),
    val createdAt : Date  = Date(),
) : Parcelable




@Parcelize
data class Performance(
    val timer : String = "",
    val endTime : String = "",
    val earning : Double = 0.0,
) : Parcelable

@Parcelize
data class QuizInfo(
    val id : String ? = null,
    val category : Category ? = null,
    val type : Subject ? = null,
    val name : String ? = null,
    val levels: Levels ? = null,
) : Parcelable

@Parcelize
data class AnswerSheet(
    val questions: Questions? = null,
    val answer: String ? = null,
    val correct : Boolean = false,
    val points : Double = 0.00
) : Parcelable

