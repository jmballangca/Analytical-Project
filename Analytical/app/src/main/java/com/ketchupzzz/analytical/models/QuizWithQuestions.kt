package com.ketchupzzz.analytical.models

import android.os.Parcelable
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.quiz.Quiz
import kotlinx.parcelize.Parcelize


@Parcelize
data class QuizWithQuestions(
    val quiz: Quiz? = null,
    val questions: List<Questions> = mutableListOf()
) : Parcelable
