package com.ketchupzzz.analytical.presentation.main.gaming.data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.models.submissions.Submissions
import kotlinx.parcelize.Parcelize


@Parcelize
data class SubmissionData(
    val title: String = "",
    val levels: Levels,
    val nextLevels: Levels ? = null,
    val quiz : Quiz,
    val submissions: Submissions,
    @DrawableRes val imageRes: Int ?= null,
    val message: String = ""
) : Parcelable
