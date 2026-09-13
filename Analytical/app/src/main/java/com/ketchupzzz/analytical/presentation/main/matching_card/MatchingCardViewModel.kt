package com.ketchupzzz.analytical.presentation.main.matching_card

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.models.submissions.AnswerSheet
import com.ketchupzzz.analytical.models.submissions.Performance
import com.ketchupzzz.analytical.models.submissions.QuizInfo
import com.ketchupzzz.analytical.models.submissions.Submissions
import com.ketchupzzz.analytical.repository.questions.QuestionRepository
import com.ketchupzzz.analytical.repository.submissions.SubmissionRepository
import com.ketchupzzz.analytical.repository.user.StudentRepository
import com.ketchupzzz.analytical.utils.UiState
import com.ketchupzzz.analytical.utils.getEarnings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MatchingCardViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val submissionRepository: SubmissionRepository,
    private val studentRepository: StudentRepository,
) : ViewModel() {
    var state by mutableStateOf(MatchingCardState())

    init {
        viewModelScope.launch {
            studentRepository.getStudent().collect { data ->
                state = state.copy(students = data)
            }
        }
    }
    fun events(e: MatchingCardEvents) {
        when (e) {
            is MatchingCardEvents.CardClicked -> onCardClicked(e.card)
            MatchingCardEvents.RestartGame -> resetGame()
            MatchingCardEvents.CheckForMatch -> {}
            is MatchingCardEvents.MatchFound -> updateMatches(e.first,e.second)
            MatchingCardEvents.ResetFlippedCards -> resetFlippedCards()
            is MatchingCardEvents.OnGetQuestions -> getQuestions(e.gameID,e.levelID,e.count)
            is MatchingCardEvents.OnAddAnswerSheet -> addPoints(e.question,e.points)
            is MatchingCardEvents.OnNext -> {
                state = state.copy(
                    isGameOver = false,
                    selectedIndex = e.index,
                    cards = state.questions[e.index].choices.toMatchingCard(),
                    matches = emptyList()
                )
                events(MatchingCardEvents.RestartGame)
            }

            is MatchingCardEvents.OnSetData -> state = state.copy(quizAndLevel = e.quizAndLevel)
            is MatchingCardEvents.OnSetStudent -> state = state.copy(
                students = e.students
            )
            is MatchingCardEvents.OnSave -> save(e.context)
        }
    }

    private fun resetGame() {
        viewModelScope.launch {
            delay(3000)
            state = state.copy(
                cards = state.cards.map { card ->
                    card.copy(isFront = false)
                }
            )
        }

    }


    private fun save(context: Context) {
        val quiz = state.quizAndLevel?.quiz
        val level = state.quizAndLevel?.level
        val submissions = Submissions(
            studentID = state.students?.id?: "",
            answerSheet = state.answerSheet,
            performance = Performance(
                timer = "0.00",
                endTime = "0.00",
                earning = state.answerSheet.getEarnings()
            ),
            quizInfo = QuizInfo(
                id = quiz?.id,
                category = quiz?.category,
                type = quiz?.subject,
                name = quiz?.title,
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
    private fun addPoints(
        questions: Questions,
        points: Int
    ) {
        val answerSheet = AnswerSheet(
            questions = questions,
            answer = "",
            correct = true,
            points = points.toDouble()
        )
        val currentAnswerSheet = state.answerSheet.toMutableList()
        currentAnswerSheet.add(answerSheet)
        state = state.copy(
            answerSheet = currentAnswerSheet,
        )
    }

    private fun updateMatches(first: String, second: String) {
        val matchFound = listOf(first,second)
        state = state.copy(
            firstFlippedCard = null,
            secondFlippedCard = null,
            matches = state.matches + matchFound,
        )
        viewModelScope.launch {
            delay(1000)
            state = state.copy(isGameOver = state.cards.all { it.isFront })
        }
    }

    private fun resetFlippedCards() {
        val updatedCards = state.cards.map { card ->
            if (card.id == state.firstFlippedCard?.id || card.id == state.secondFlippedCard?.id) {
                card.copy(isFront = false)
            } else card
        }
        state = state.copy(
            cards = updatedCards,
            firstFlippedCard = null,
            secondFlippedCard = null,
        )
    }

    private fun onCardClicked(card: MatchingCard) {
        if (state.firstFlippedCard == null) {
            state = state.copy(
                firstFlippedCard = card
            )
            flipCard(card)
        } else if (state.secondFlippedCard == null && card.id != state.firstFlippedCard?.id) {
            state = state.copy(
                secondFlippedCard = card
            )
            flipCard(card)
        } else {
            state = state.copy(
                firstFlippedCard =  null
            )
            flipCard(card)
        }
    }

    private fun flipCard(card: MatchingCard) {
        val updatedCards = state.cards.map {
            if (it.globalIndex == card.globalIndex) it.copy(isFront = !card.isFront) else it
        }
        state = state.copy(cards = updatedCards)
    }
    private fun getQuestions(gameID : String,levelID : String, count: Int) {
        viewModelScope.launch {
            questionRepository.getRandomQuestions(gameID,levelID,count) {
                 when(it) {
                    is UiState.Error ->state = state.copy(
                        isLoading = false,
                        errors = it.message
                    )
                    is UiState.Loading -> state = state.copy(isLoading = true, errors = null)
                    is UiState.Success -> {

                        val shuffledQuestions = it.data.shuffled()
                        val initialCards = shuffledQuestions.firstOrNull()?.choices?.toMatchingCard() ?: emptyList()
                        state = state.copy(
                            isLoading = false,
                            questions = shuffledQuestions,
                            errors = null,
                            cards = initialCards,
                            matches = emptyList()
                        )

                        events(MatchingCardEvents.RestartGame)



                    }
                }
            }


        }
    }
}