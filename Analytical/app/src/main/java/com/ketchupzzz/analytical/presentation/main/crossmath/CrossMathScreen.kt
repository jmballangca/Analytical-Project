package com.ketchupzzz.analytical.presentation.main.crossmath

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.ketchupzzz.analytical.R
import com.ketchupzzz.analytical.presentation.main.crossmath.components.Calculator
import com.ketchupzzz.analytical.presentation.main.crossmath.components.CorrectScreenDialog
import com.ketchupzzz.analytical.presentation.main.games.data.QuizAndLevel
import com.ketchupzzz.analytical.presentation.main.gaming.components.HintDialog
import com.ketchupzzz.analytical.utils.ProgressBar
import com.ketchupzzz.analytical.utils.UnknownError
import com.ketchupzzz.analytical.utils.getEarnings
import com.ketchupzzz.analytical.utils.toast
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrossMathScreen(
    modifier: Modifier = Modifier,
    quiz : QuizAndLevel,
    state: CrossMathState,
    events: (CrossMathEvents) -> Unit,
    navHostController: NavHostController
    ) {
    val context = LocalContext.current
    var isCorrect by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(state.isGameEnd) {
        if (state.isGameEnd) {
            context.toast("Your score has been submitted")
            delay(1000)
            navHostController.popBackStack()
        }
    }
    LaunchedEffect(quiz) {
        if (quiz.quiz.id != null) {
            events(CrossMathEvents.OnSetData(quiz))
            events(CrossMathEvents.OnGetCrossMathQuestion(
                quizID = quiz.quiz.id,
                levelID = quiz.level.id,
                timer = quiz.level.timer,
                count = quiz.level.questions
            ))
        }
    }
    val question = state.questions.getOrNull(state.selectedIndex)
    if (isCorrect) {
        val isLast = state.selectedIndex == state.questions.size -1
        CorrectScreenDialog(
            message = "Your answer is correct. You earn +${quiz.level.points}",
            onSubmit = {
                isCorrect = !isCorrect
                events.invoke(CrossMathEvents.OnSave(
                    level = quiz.level,
                    quiz = quiz.quiz,
                    context = context,
                ))
            },
            isLast = isLast,
            onNext = {
                isCorrect = !isCorrect
                if (!isLast){
                    events(CrossMathEvents.OnNext(
                        state.selectedIndex + 1
                    ))
                }
            },
            onDismiss = {
                isCorrect = !isCorrect
            },
            maxPoints = quiz.level.questions * quiz.level.points,
            totalPoints = state.answerSheet.getEarnings()
        )
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Question ${state.selectedIndex + 1}") },
                navigationIcon = {
                    IconButton(
                        onClick = { navHostController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }

            )
        }
    ) {

        when {
            state.isLoading -> ProgressBar(
                title = "Getting All Questions!"
            )
            state.errors != null -> UnknownError(
                title = "${state.errors}"
            ) { Button(onClick = {navHostController.popBackStack()}) { Text("Back") } }
            else -> {
                if (state.questions.isEmpty()) {
                    UnknownError(
                        title = "No Questions Yet"
                    ) { Button(onClick = {navHostController.popBackStack()}) { Text("Back") }
                }
            } else {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(it)
                ) {

                    Column(
                        modifier = modifier
                            .fillMaxSize()
                            .weight(1f)
                            .padding(8.dp),
                    ) {
                        AsyncImage(
                            model = question?.image,
                            contentDescription = "",
                            placeholder = painterResource(R.drawable.logo),
                            modifier = modifier
                                .fillMaxSize()
                                .weight(1f)
                        )

                        Text(
                            state.wrongAnswer ?: "",
                            modifier = modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurface)
                        )

                    }
                    Calculator(
                        value = state.answer,
                        onChange = {events(CrossMathEvents.OnAnswerChange(it))},
                        onDelete = {events(CrossMathEvents.OnDeleteAnswer)},
                        onSubmit = {
                            events(CrossMathEvents.OnSubmit)
                            if (state.answer == question?.answer) {
                                isCorrect = !isCorrect
                            }},
                        hint = question?.hint ?: ""
                    )
                }
            }
        }
    }
    }
}



