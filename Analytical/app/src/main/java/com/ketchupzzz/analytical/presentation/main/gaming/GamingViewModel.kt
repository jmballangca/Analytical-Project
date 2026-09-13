package com.ketchupzzz.analytical.presentation.main.gaming

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.models.submissions.AnswerSheet
import com.ketchupzzz.analytical.models.submissions.Performance
import com.ketchupzzz.analytical.models.submissions.QuizInfo
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.repository.questions.QuestionRepository
import com.ketchupzzz.analytical.repository.submissions.SubmissionRepository
import com.ketchupzzz.analytical.repository.user.StudentRepository
import com.ketchupzzz.analytical.utils.UiState
import com.ketchupzzz.analytical.utils.getEarnings
import com.ketchupzzz.analytical.utils.getEndMinute
import com.ketchupzzz.analytical.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class GamingViewModel @Inject constructor(
     private val questionRepository: QuestionRepository,
     private val studentRepository: StudentRepository,
     private val submissionsRepository: SubmissionRepository
) : ViewModel() {
     var state by mutableStateOf(GamingState())
     init {
          viewModelScope.launch {
               studentRepository.getStudent().collect { data ->
                    state = state.copy(students = data)
               }
          }
     }
     fun events(e : GamingEvents) {
          when(e) {
               is GamingEvents.OnGetQuestions -> getQuestions(
                    e.gameID,
                    e.levelID,
                    e.count,
                    e.timer
               )
               is GamingEvents.OnExitGame -> exit(e.navHostController)
               is GamingEvents.OnSelectAnswer -> updateAnswerSheet(e.questions,e.answer,e.points)
               is GamingEvents.OnSubmit -> submit(e.level,e.quiz,e.context)
               is GamingEvents.OnUpdateTimer -> state = state.copy(timer = e.time, isFinish = e.time == 0)
               is GamingEvents.OnAnswerChanged -> addAnswer(e.text,e.index)
               GamingEvents.OnReset -> reset()
          }
     }

     private fun addAnswer(text: String, index: Int) {
          val current = state.answerIndex.toMutableList()
          current.add(index)
          state = state.copy(
               answers = state.answers + text,
               answerIndex = current
          )
     }
     private fun reset() {
          state = state.copy(
               answers = "",
               answerIndex = emptyList()
          )
     }
     private fun submit(
          level: Levels,
          quiz: Quiz,
          context: Context
     ) {
          val submissions = Submissions(
               studentID = state.students?.id?: "",
               answerSheet = state.answerSheet,
               performance = Performance(
                    timer = (level.timer * 60).getEndMinute(),
                    endTime = state.timer.getEndMinute(),
                    earning = state.answerSheet.getEarnings()
               ),
               quizInfo = QuizInfo(
                    id = quiz.id,
                    category = quiz.category,
                    type = quiz.subject,
                    name = quiz.title,
                    levels = level
               )


          )

          viewModelScope.launch {
               submissionsRepository.submitQuiz(submissions) {
                    when(it) {
                         is UiState.Error -> state = state.copy(
                              isLoading = false,
                              errors = it.message
                         )
                         UiState.Loading -> state = state.copy(
                              isLoading = true,
                              errors = null,
                         )
                         is UiState.Success -> {
                              context.toast(it.data)
                              state = state.copy(
                                   isLoading = false,
                                   errors = null,
                                   isFinish = true
                              )
                         }
                    }
               }
          }
     }

     private fun updateAnswerSheet(questions: Questions, answer: String,points : Int) {
          val isCorrect = questions.answer?.trim()?.lowercase() == answer.trim().lowercase()
          val answerSheet  = AnswerSheet(
               questions = questions,
               answer = answer,
               correct = isCorrect,
               points = (if (isCorrect) points else 0).toDouble()
          )
          val updatedAnswers = state.answerSheet.toMutableList().apply {
               add(answerSheet)
          }
          state = state.copy(
               answerSheet = updatedAnswers,
               isFinish = updatedAnswers.size == state.questionCount
          )
     }

     private fun exit(navHostController: NavHostController) {
          navHostController.popBackStack()
          navHostController.popBackStack()
     }

     private fun getQuestions(gameID : String,levelID : String, count: Int,timer : Int) {
          viewModelScope.launch {
               questionRepository.getRandomQuestions(gameID,levelID,count) {
                    state = when(it) {
                         is UiState.Error -> state.copy(
                              isLoading = false,
                              errors = it.message
                         )
                         is UiState.Loading -> state.copy(isLoading = true, errors = null)
                         is UiState.Success -> state.copy(
                              isLoading = false,
                              questions = it.data.shuffled(),
                              timer = timer * 60,
                              errors = null,
                              questionCount = count
                         )
                    }
               }
          }
     }
}