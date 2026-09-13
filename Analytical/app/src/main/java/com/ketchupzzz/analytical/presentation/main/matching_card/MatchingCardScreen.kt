package com.ketchupzzz.analytical.presentation.main.matching_card

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.models.questions.Questions
import com.ketchupzzz.analytical.presentation.main.crossmath.CrossMathEvents
import com.ketchupzzz.analytical.presentation.main.crossmath.components.CorrectScreenDialog
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel
import com.ketchupzzz.analytical.utils.ProgressBar
import com.ketchupzzz.analytical.utils.UnknownError
import com.ketchupzzz.analytical.utils.getEarnings
import com.ketchupzzz.analytical.utils.toast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sqrt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MatchingCardScreen(
    modifier: Modifier = Modifier,
    args: QuizAndLevel,
    state: MatchingCardState,
    events: (MatchingCardEvents) -> Unit,
    navHostController: NavHostController
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = state.selectedIndex) {args.level.questions}
    val context = LocalContext.current

    var correctScreenDialog  by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(args) {
        if (args.quiz.id != null) {
            events(MatchingCardEvents.OnSetData(args))
        }
    }
    val question = state.questions.getOrNull(state.selectedIndex)
    LaunchedEffect(state.isGameEnd) {
        if (state.isGameEnd) {
            context.toast("Your Score has been submitted")
            delay(1000)
            navHostController.popBackStack()
        }
    }
    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) {
            question?.let {
                events.invoke(MatchingCardEvents.OnAddAnswerSheet(it, points =args.level.points))
            }
        }

    }

    if (state.isGameOver && !correctScreenDialog) {
        correctScreenDialog = true
    }
    if (correctScreenDialog) {
        val isLast = state.selectedIndex == state.questions.size -1
        if  (isLast) {
            events.invoke(MatchingCardEvents.OnSave(context))
        }
        CorrectScreenDialog(
            message = "Your answer is correct. You earn +${args.level.points}",
            onSubmit = {
                correctScreenDialog = !correctScreenDialog

            },
            isLast = isLast,
            onNext = {
                correctScreenDialog = !correctScreenDialog
                if (!isLast){
                    events(MatchingCardEvents.OnNext(state.selectedIndex + 1))
                }
            },
            onDismiss = {
                correctScreenDialog = !correctScreenDialog
            },
            maxPoints = args.level.questions * args.level.points,
            totalPoints = state.answerSheet.getEarnings()
        )
    }





    LaunchedEffect(args) {
        events(MatchingCardEvents.OnGetQuestions(
            gameID = args.quiz.id!!,
            levelID = args.level.id,
            count = args.level.questions
        ))
    }
    LaunchedEffect(state.firstFlippedCard, state.secondFlippedCard) {
        if (state.firstFlippedCard != null && state.secondFlippedCard != null) {
            if (state.firstFlippedCard.choice == state.secondFlippedCard.choice) {
                events(MatchingCardEvents.MatchFound(state.firstFlippedCard.id, state.secondFlippedCard.id))
            } else {
                delay(1000)
                events(MatchingCardEvents.ResetFlippedCards)
            }
        }
    }

    LaunchedEffect(pagerState.currentPage,state.questions){
        if (state.questions.isNotEmpty()) {
            val questions = state.questions[pagerState.currentPage]
            val cards = questions.choices.toMatchingCard()
            events.invoke(MatchingCardEvents.RestartGame)
        }
    }
    Scaffold {
        Box {
            when {
                state.isLoading -> ProgressBar(title = "Loading....")
                state.errors != null->  UnknownError(
                    title = "No Questions Yet!"
                ) { Button(onClick = {
                    navHostController.popBackStack()
                    navHostController.popBackStack()
                }){ Text("Back") }}

                else -> {
                    if (state.questions.isEmpty()) {
                        UnknownError(
                            title = "No Questions Yet!"
                        ) { Button(onClick = {
                            navHostController.popBackStack()
                            navHostController.popBackStack()
                        }){ Text("Back") } }
                    } else {
                        if (state.cards.isEmpty()) {
                            Box {
                                Text("card is empty")
                            }
                        } else {
                            HorizontalPager(
                                pagerState,
                                modifier = modifier.fillMaxSize().padding(it),
                                userScrollEnabled = false,
                            ) {

                                MatchingCardLayout(
                                    cards = state.cards,
                                    chucked = state.questions[state.selectedIndex].choices.size,
                                    matches = state.matches,
                                    onFlip = { events.invoke(MatchingCardEvents.CardClicked(it)) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MatchingCardLayout(
    modifier: Modifier = Modifier,
    chucked: Int,
    cards: List<MatchingCard>,
    matches: List<String>,
    onFlip: (MatchingCard) -> Unit
) {
    // Calculate the number of columns and rows
    val totalCards = chucked * 2
    val columns = sqrt(totalCards.toDouble()).toInt()
    val rows = (totalCards + columns - 1) / columns

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Split cards into rows
        cards.chunked(columns).forEach { items ->
            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.Center
            ) {
                items.forEach { card ->
                    CardFlip(
                        card = card,
                        haveMatch = matches.contains(card.id),
                        onFlip = { onFlip(card) }
                    )
                }
            }
        }
    }
}



