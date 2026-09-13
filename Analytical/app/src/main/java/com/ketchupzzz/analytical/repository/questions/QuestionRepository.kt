package com.ketchupzzz.analytical.repository.questions

import com.ketchupzzz.analytical.models.Category
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.QuizWithQuestions
import com.ketchupzzz.analytical.utils.UiState

interface QuestionRepository {

    fun getAllQuestionByID(questionID: String,result : (UiState<List<Questions>>) -> Unit)
    fun getQuizWithQuestions(quizID: String,result : (UiState<QuizWithQuestions>) -> Unit)
    suspend fun getRandomQuestions(
        gameID : String,
        levelID : String,
        count : Int,
        result: (UiState<List<Questions>>) -> Unit
    )
}