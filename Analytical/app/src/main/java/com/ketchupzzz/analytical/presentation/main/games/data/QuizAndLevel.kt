package com.ketchupzzz.analytical.presentation.main.games.data

import android.os.Parcelable
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz
import kotlinx.parcelize.Parcelize


@Parcelize
data class QuizAndLevel(
    val quiz: Quiz,
    val level : Levels,
    val nextLevels: Levels ? = null
) : Parcelable


