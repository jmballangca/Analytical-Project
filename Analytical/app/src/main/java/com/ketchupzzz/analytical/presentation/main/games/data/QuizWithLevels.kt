package com.ketchupzzz.analytical.presentation.main.games.data

import android.os.Parcelable
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.models.submissions.Submissions
import kotlinx.parcelize.Parcelize


@Parcelize
data class QuizWithLevels(
    val quiz : Quiz ? = null,
    val levels: List<Levels> = emptyList(),
    val submissions : List<Submissions> = emptyList()
) : Parcelable
