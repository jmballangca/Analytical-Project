package com.ketchupzzz.analytical.repository.submissions

import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.presentation.main.games.data.LevelsWithSubmissions
import com.ketchupzzz.analytical.utils.UiState



interface SubmissionRepository {
    suspend fun submitQuiz(submission: Submissions, result : (UiState<String>) -> Unit)
    suspend fun getAllSubmissions(id : String,result: (UiState<List<Submissions>>) -> Unit)
    suspend fun getAllSubmissions(result: (UiState<List<Submissions>>) -> Unit)
}