package com.ketchupzzz.analytical.presentation.main.crossmath

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.analytical.models.quiz.Levels
import com.ketchupzzz.analytical.models.quiz.Quiz
import com.ketchupzzz.analytical.models.submissions.AnswerSheet
import com.ketchupzzz.analytical.models.submissions.Performance
import com.ketchupzzz.analytical.models.submissions.QuizInfo
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.repository.questions.QuestionRepository
import com.ketchupzzz.analytical.repository.quiz.QuizRepository
import com.ketchupzzz.analytical.repository.submissions.SubmissionRepository
import com.ketchupzzz.analytical.repository.user.StudentRepository
import com.ketchupzzz.analytical.utils.UiState
import com.ketchupzzz.analytical.utils.getEarnings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CrossMathViewModel @Inject constructor(
    private val quizRepository : QuizRepository,
    private val questionRepository: QuestionRepository,
    private val submissionRepository: SubmissionRepository,
    private val studentRepository: StudentRepository,
) : ViewModel() {
    var state by mutableStateOf(CrossMathState())
    init {
        viewModelScope.launch {
            studentRepository.getStudent().collect { data ->
                state = state.copy(students = data)
            }
        }
    }
    fun events(e : CrossMathEvents) {
        when(e) {
            is CrossMathEvents.OnGetCrossMathQuestion -> getQuestions(e.quizID,e.levelID,e.count,e.timer)

            is CrossMathEvents.OnAnswerChange -> state = state.copy(
                answer = state.answer + e.ans
            )
            CrossMathEvents.OnDeleteAnswer -> reset()
            CrossMathEvents.OnSubmit -> submit()
            is CrossMathEvents.OnSetData -> state = state.copy(
               quizAndLevel = e.quizAndLevel
            )

            is CrossMathEvents.OnNext -> {
                events(CrossMathEvents.OnSubmit)
                state = state.copy(
                    selectedIndex = e.index
                )
            }

            is CrossMathEvents.OnSave -> submit(
                e.level,
                e.quiz,
                e.context
            )
            is CrossMathEvents.OnSetStudent -> state= state.copy(
                students = e.students
            )
        }
    }

    private fun submit() {
        val isGameEnd = state.questions.size - 1 == state.selectedIndex
        val question = state.questions[state.selectedIndex]

        val isCorrect = state.answer == question.answer
        if (!isCorrect) {
            state = state.copy(wrongAnswer = "Wrong answer!")
            return
        }
        val points = state.quizAndLevel?.level?.points
        val answerSheet = AnswerSheet(
            questions = question,
            answer = state.answer,
            correct = true,
            points = state.quizAndLevel?.level?.points?.toDouble() ?: 0.00
        )
        val currentAnswerSheet = state.answerSheet.toMutableList()
        currentAnswerSheet.add(answerSheet)
        state = state.copy(
            answerSheet = currentAnswerSheet,
        )
        reset()
    }

    private fun reset() {
        state = state.copy(
            answer = "",
            wrongAnswer = null
        )
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
                        errors = null,
                    )
                }
            }
        }
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
                timer = "0.00",
                endTime = "0.00",
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
            submissionRepository.submitQuiz(submissions) {
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
                        state = state.copy(
                            isLoading = false,
                            errors = null,
                            isGameEnd = true
                        )
                    }
                }
            }
        }
    }
}